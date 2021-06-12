package com.craxiom.networksurvey.logging;

import android.os.Looper;

import com.craxiom.messaging.WifiBeaconRecordData;
import com.craxiom.messaging.wifi.CipherSuite;
import com.craxiom.messaging.wifi.EncryptionType;
import com.craxiom.networksurvey.constants.NetworkSurveyConstants;
import com.craxiom.networksurvey.constants.WifiBeaconMessageConstants;
import com.craxiom.networksurvey.listeners.IWifiSurveyRecordListener;
import com.craxiom.networksurvey.model.WifiRecordWrapper;
import com.craxiom.networksurvey.services.NetworkSurveyService;
import com.craxiom.networksurvey.util.IOUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.sf.Point;
import timber.log.Timber;

/**
 * Responsible for taking 802.11 survey records, and writing them to the GeoPackage log file.
 *
 * @since 0.1.2
 */
public class WifiSurveyRecordLogger extends SurveyRecordLogger implements IWifiSurveyRecordListener
{
    /**
     * Constructs a Logger that writes 802.11 Survey records to a GeoPackage SQLite database.
     *
     * @param networkSurveyService The Service instance that is running this logger.
     * @param serviceLooper        The Looper associated with the service that can be used to do any background processing.
     */
    public WifiSurveyRecordLogger(NetworkSurveyService networkSurveyService, Looper serviceLooper)
    {
        super(networkSurveyService, serviceLooper, NetworkSurveyConstants.LOG_DIRECTORY_NAME, NetworkSurveyConstants.WIFI_FILE_NAME_PREFIX);
    }

    @Override
    public void onWifiBeaconSurveyRecords(List<WifiRecordWrapper> wifiBeaconRecords)
    {
        wifiBeaconRecords.forEach(this::writeWifiBeaconRecordToLogFile);
    }

    @Override
    void createTables(GeoPackage geoPackage, SpatialReferenceSystem srs) throws SQLException
    {
        createWifiBeaconRecordTable(geoPackage, srs);
    }

    /**
     * Creates an GeoPackage Table that can be populated with 802.11 Beacon Records.
     *
     * @param geoPackage The GeoPackage to create the table in.
     * @param srs        The SRS to use for the table coordinates.
     * @throws SQLException If there is a problem working with the GeoPackage SQLite DB.
     */
    private void createWifiBeaconRecordTable(GeoPackage geoPackage, SpatialReferenceSystem srs) throws SQLException
    {
        createTable(WifiBeaconMessageConstants.WIFI_BEACON_RECORDS_TABLE_NAME, geoPackage, srs, false, (tableColumns, columnNumber) -> {
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.BSSID_COLUMN, GeoPackageDataType.TEXT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.SSID_COLUMN, GeoPackageDataType.TEXT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.CHANNEL_COLUMN, GeoPackageDataType.SMALLINT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.FREQUENCY_MHZ_COLUMN, GeoPackageDataType.MEDIUMINT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.CIPHER_SUITES_COLUMN, GeoPackageDataType.TEXT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.AKM_SUITES_COLUMN, GeoPackageDataType.TEXT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.ENCRYPTION_TYPE_COLUMN, GeoPackageDataType.TEXT, false, null));
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.WPS_COLUMN, GeoPackageDataType.BOOLEAN, false, null));
            //noinspection UnusedAssignment
            tableColumns.add(FeatureColumn.createColumn(columnNumber++, WifiBeaconMessageConstants.SIGNAL_STRENGTH_COLUMN, GeoPackageDataType.FLOAT, false, null));
        });
    }

    /**
     * Given an 802.11 Beacon Record, write it to the GeoPackage log file.
     *
     * @param wifiRecordWrapper The 802.11 Beacon Record to write to the log file.
     */
    private void writeWifiBeaconRecordToLogFile(final WifiRecordWrapper wifiRecordWrapper)
    {
        if (!loggingEnabled) return;

        handler.post(() -> {
            synchronized (geoPackageLock)
            {
                try
                {
                    if (geoPackage != null)
                    {
                        final WifiBeaconRecordData data = wifiRecordWrapper.getWifiBeaconRecord().getData();
                        FeatureDao featureDao = geoPackage.getFeatureDao(WifiBeaconMessageConstants.WIFI_BEACON_RECORDS_TABLE_NAME);
                        FeatureRow row = featureDao.newRow();

                        Point fix = new Point(data.getLongitude(), data.getLatitude(), (double) data.getAltitude());

                        GeoPackageGeometryData geomData = new GeoPackageGeometryData(WGS84_SRS);
                        geomData.setGeometry(fix);

                        row.setGeometry(geomData);

                        row.setValue(WifiBeaconMessageConstants.TIME_COLUMN, IOUtils.getEpochFromRfc3339(data.getDeviceTime()));
                        row.setValue(WifiBeaconMessageConstants.MISSION_ID_COLUMN, data.getMissionId());
                        row.setValue(WifiBeaconMessageConstants.RECORD_NUMBER_COLUMN, data.getRecordNumber());

                        final String sourceAddress = data.getSourceAddress();
                        if (!sourceAddress.isEmpty())
                        {
                            row.setValue(WifiBeaconMessageConstants.SOURCE_ADDRESS_COLUMN, sourceAddress);
                        }

                        final String bssid = data.getBssid();
                        if (!bssid.isEmpty())
                        {
                            row.setValue(WifiBeaconMessageConstants.BSSID_COLUMN, bssid);
                        }

                        final String ssid = data.getSsid();
                        if (!ssid.isEmpty())
                        {
                            row.setValue(WifiBeaconMessageConstants.SSID_COLUMN, ssid);
                        }

                        if (data.hasSignalStrength())
                        {
                            row.setValue(WifiBeaconMessageConstants.SIGNAL_STRENGTH_COLUMN, data.getSignalStrength().getValue());
                        }

                        if (data.hasChannel())
                        {
                            setShortValue(row, WifiBeaconMessageConstants.CHANNEL_COLUMN, data.getChannel().getValue());
                        }

                        if (data.hasFrequencyMhz())
                        {
                            setIntValue(row, WifiBeaconMessageConstants.FREQUENCY_MHZ_COLUMN, data.getFrequencyMhz().getValue());
                        }

                        final EncryptionType encryptionType = data.getEncryptionType();
                        if (encryptionType != EncryptionType.UNKNOWN)
                        {
                            row.setValue(WifiBeaconMessageConstants.ENCRYPTION_TYPE_COLUMN, WifiBeaconMessageConstants.getEncryptionTypeString(encryptionType));
                        }

                        if (data.hasWps())
                        {
                            row.setValue(WifiBeaconMessageConstants.WPS_COLUMN, data.getWps().getValue());
                        }

                        final List<CipherSuite> cipherSuitesList = data.getCipherSuitesList();
                        if (!cipherSuitesList.isEmpty())
                        {
                            row.setValue(WifiBeaconMessageConstants.CIPHER_SUITES_COLUMN,
                                    cipherSuitesList.stream().map(WifiBeaconMessageConstants::getCipherSuiteString)
                                            .collect(Collectors.joining(";")));
                        }

                        featureDao.insert(row);

                        checkIfRolloverNeeded();
                    }
                } catch (Exception e)
                {
                    Timber.e(e, "Something went wrong when trying to write a Wi-Fi survey record");
                }
            }
        });
    }
}

package com.craxiom.networksurvey;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.craxiom.networksurvey.logging.SurveyRecordLogger;
import com.craxiom.networksurvey.services.NetworkSurveyService;

/**
 * A GPS Listener that is registered with the Android Location Service so that we are notified of Location updates.
 * <p>
 * This code was modeled after the WiGLE Wi-Fi App GPSListener:
 * https://github.com/wiglenet/wigle-wifi-wardriving/blob/master/wiglewifiwardriving/src/main/java/net/wigle/wigleandroid/listener/GPSListener.java
 *
 * @since 0.0.1
 */
public class GpsListener implements LocationListener
{
    private static final String LOG_TAG = GpsListener.class.getSimpleName();
    private static final float MIN_DISTANCE_ACCURACY = 32f; // This is the number that WiGLE Wi-Fi uses.

    private Location latestLocation;
    private NetworkSurveyService networkSurveyService;

    @Override
    public void onLocationChanged(Location location)
    {
        updateLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.i(LOG_TAG, "Location Provider (" + provider + ") has been enabled");
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.i(LOG_TAG, "Location Provider (" + provider + ") has been disabled");

        if (LocationManager.GPS_PROVIDER.equals(provider)) latestLocation = null;
    }

    public Location getLatestLocation()
    {
        return latestLocation;
    }

    /**
     * The {@link com.craxiom.networksurvey.services.GnssGeoPackageRecorder} works differently than the
     * {@link SurveyRecordLogger}.  The GNSS logger expects to be notified of the new location which triggers a write to
     * the GeoPackage file.  This method allows the GNSS logger to get notified anytime the location update occurs.
     * <p>
     * Likely we need to unify the two approaches.
     *
     * @param networkSurveyService The listener wanted to get notified of location changes.
     */
    public void addLocationListener(NetworkSurveyService networkSurveyService)
    {
        this.networkSurveyService = networkSurveyService;
    }

    public void removeLocationListener()
    {
        networkSurveyService = null;
    }

    /**
     * Updates the cached location with the newly provided location.
     *
     * @param newLocation The newly provided location.
     */
    private void updateLocation(Location newLocation)
    {
        if (newLocation != null && LocationManager.GPS_PROVIDER.equals(newLocation.getProvider())
                && newLocation.getAccuracy() <= MIN_DISTANCE_ACCURACY)
        {
            latestLocation = newLocation;
            if (networkSurveyService != null) networkSurveyService.updateLocation(newLocation);
        } else
        {
            Log.d(LOG_TAG, "The accuracy of the last GPS location is less than the required minimum");
            latestLocation = null;
        }
    }
}

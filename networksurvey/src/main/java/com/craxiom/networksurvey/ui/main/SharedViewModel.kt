package com.craxiom.networksurvey.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.craxiom.messaging.BluetoothRecordData
import com.craxiom.networksurvey.fragments.model.MqttConnectionSettings
import com.craxiom.networksurvey.model.WifiNetwork
import com.craxiom.networksurvey.ui.cellular.model.ServingCellInfo
import com.craxiom.networksurvey.ui.wifi.model.WifiNetworkInfoList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    private val _navigateToQrCodeScanner = MutableLiveData(false)
    val navigateToQrCodeScanner: LiveData<Boolean> = _navigateToQrCodeScanner

    private val _navigateToQrCodeShare = MutableLiveData(false)
    val navigateToQrCodeShare: LiveData<Boolean> = _navigateToQrCodeShare

    private val _navigateToTowerMap = MutableLiveData(false)
    val navigateToTowerMap: LiveData<Boolean> = _navigateToTowerMap
    private var _latestServingCellInfo: ServingCellInfo? = null
    val latestServingCellInfo: ServingCellInfo?
        get() = _latestServingCellInfo

    /*private val _navigateToWifiSpectrum = MutableLiveData(false)
    val navigateToWifiSpectrum: LiveData<Boolean> = _navigateToWifiSpectrum*/
    private var _wifiNetworkList: WifiNetworkInfoList? = null
    val wifiNetworkList: WifiNetworkInfoList?
        get() = _wifiNetworkList

    private val _navigateToWifiDetails = MutableLiveData(false)
    val navigateToWifiDetails: LiveData<Boolean> = _navigateToWifiDetails
    private var _wifiNetwork: WifiNetwork? = null
    val wifiNetwork: WifiNetwork?
        get() = _wifiNetwork

    private val _navigateToBluetoothDetails = MutableLiveData(false)
    val navigateToBluetoothDetails: LiveData<Boolean> = _navigateToBluetoothDetails
    private var _bluetoothData: BluetoothRecordData? = null
    val bluetoothData: BluetoothRecordData?
        get() = _bluetoothData

    private val _navigateToMqttConnection = MutableLiveData(false)
    val navigateToMqttConnection: LiveData<Boolean> = _navigateToMqttConnection
    private var _mqttConnectionSettings: MqttConnectionSettings? = null
    val mqttConnectionSettings: MqttConnectionSettings?
        get() = _mqttConnectionSettings

    private val _navigateToSettings = MutableLiveData(false)
    val navigateToSettings: LiveData<Boolean> = _navigateToSettings

    fun triggerNavigationToQrCodeScanner(mqttConnectionSettings: MqttConnectionSettings) {
        _mqttConnectionSettings = mqttConnectionSettings
        _navigateToQrCodeScanner.value = true
    }

    fun triggerNavigationToQrCodeShare(mqttConnectionSettings: MqttConnectionSettings) {
        _mqttConnectionSettings = mqttConnectionSettings
        _navigateToQrCodeShare.value = true
    }

    fun updateLatestServingCellInfo(servingCellInfo: ServingCellInfo) {
        _latestServingCellInfo = servingCellInfo
    }

    fun triggerNavigationToTowerMap() {
        _navigateToTowerMap.value = true
    }

    fun updateWifiNetworkInfoList(wifiNetworkInfoList: WifiNetworkInfoList) {
        _wifiNetworkList = wifiNetworkInfoList
    }

    /*fun triggerNavigationToWifiSpectrum() {
        _navigateToWifiSpectrum.value = true
    }*/

    fun triggerNavigationToWifiDetails(wifiNetwork: WifiNetwork) {
        _wifiNetwork = wifiNetwork
        _navigateToWifiDetails.value = true
    }

    fun triggerNavigationToBluetooth(bluetoothRecordData: BluetoothRecordData) {
        _bluetoothData = bluetoothRecordData
        _navigateToBluetoothDetails.value = true
    }

    fun triggerNavigationToMqttConnection() {
        _navigateToMqttConnection.value = true
    }

    fun triggerNavigationToMqttConnection(mqttConnectionSettings: MqttConnectionSettings?) {
        _mqttConnectionSettings = mqttConnectionSettings
        _navigateToMqttConnection.value = true
    }

    fun triggerNavigationToSettings() {
        _navigateToSettings.value = true
    }

    fun resetNavigationFlag() {
        // TODO is this a good idea to set them all in one method?
        _navigateToQrCodeScanner.value = false
        _navigateToQrCodeShare.value = false
        _navigateToTowerMap.value = false
        //_navigateToWifiSpectrum.value = false
        _navigateToWifiDetails.value = false
        _navigateToBluetoothDetails.value = false
        _navigateToMqttConnection.value = false
        _navigateToSettings.value = false
    }

    fun resetMqttConnectionSettings() {
        _mqttConnectionSettings = null
    }
}
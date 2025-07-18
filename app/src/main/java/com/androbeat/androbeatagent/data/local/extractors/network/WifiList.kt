package com.androbeat.androbeatagent.data.local.extractors.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.extractors.network.WifiModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.utils.ApplicationStatus

class WifiList(private val context: Context) : DataExtractor, DataProvider<List<WifiModel?>?> {
    private val _wifiList = mutableListOf<WifiModel>()
    private val wifiList: List<WifiModel>
        get() = _wifiList



    fun getWifiListStatistics(): String {
        _wifiList.clear()
        val builder = StringBuilder("Wifi list{")
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            try {
                val scanResults = wifiManager.scanResults
                for (scanResult in scanResults) {
                    val wifiModel = WifiModel()
                    wifiModel.ssid = scanResult.SSID
                    wifiModel.bssid = scanResult.BSSID
                    _wifiList.add(wifiModel)
                    builder.append(String.format("%s %s ", scanResult.SSID, scanResult.BSSID))
                }
                builder.append("}")
            } catch (e: SecurityException) {
                ApplicationStatus.postError("Error obtaining WIFI statistics: ${e.message}")
                Logger.logError(TAG, "Error: " + e.message)
            }
            ApplicationStatus.postOK()
        }
        return builder.toString()
    }

    override val statistics: String
        get() = this.getWifiListStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() =  DataProviderType.WIFI_LIST
    override val data: List<WifiModel?>
        get() = wifiList

    companion object {
        private val TAG = WifiList::class.java.simpleName
    }


}
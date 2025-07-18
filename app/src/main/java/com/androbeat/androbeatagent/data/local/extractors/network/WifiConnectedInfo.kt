package com.androbeat.androbeatagent.data.local.extractors.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.extractors.network.WifiModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor

class WifiConnectedInfo(private val context: Context) : DataExtractor, DataProvider<WifiModel?> {
    private val stats = WifiModel()



    private fun getWifiStatistics(): String {
        return try {
            val wifiInfo = getWifiInfo()
            if (wifiInfo != null) {
                stats.ssid = wifiInfo.ssid
                stats.bssid = wifiInfo.bssid
                "Wifi connected {${stats.ssid} ${stats.bssid}}"
            } else {
                "No Wifi connection"
            }
        } catch (e: Exception) {
            Logger.logError(TAG, "Error getting Wifi statistic $e")
            "Error retrieving Wifi information"
        }
    }

    private fun getWifiInfo(): WifiInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val n = cm?.activeNetwork ?: return null
        val netCaps = cm.getNetworkCapabilities(n) ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            netCaps.transportInfo as? WifiInfo
        } else {
            (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.connectionInfo
        }
    }

    override val statistics: String
        get() = this.getWifiStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.CONNECTED_WIFI
    override val data: WifiModel
        get() = stats

    companion object {
        private val TAG = WifiConnectedInfo::class.java.simpleName
    }


}
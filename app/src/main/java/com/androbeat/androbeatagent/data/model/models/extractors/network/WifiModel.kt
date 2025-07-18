package com.androbeat.androbeatagent.data.model.models.extractors.network

class WifiModel {
    var ssid: String? = null
    var bssid: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val wifiModel = other as WifiModel

        if (ssid != wifiModel.ssid) return false
        return bssid == wifiModel.bssid
    }
    override fun hashCode(): Int {
        var result = ssid?.hashCode() ?: 0
        result = 31 * result + (bssid?.hashCode() ?: 0)
        return result
    }
}
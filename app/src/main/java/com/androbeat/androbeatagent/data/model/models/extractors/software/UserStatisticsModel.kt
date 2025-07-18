package com.androbeat.androbeatagent.data.model.models.extractors.software

class UserStatisticsModel {
    var packet: String? = null
    var totalTimeInForeground: Long = 0
    var lastTimeUsed: Long = 0
    var describeContents = 0
    var appBytes: Long = 0
    var dataBytes: Long = 0
    var cacheBytes: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserStatisticsModel) return false

        if (packet != other.packet) return false
        if (totalTimeInForeground != other.totalTimeInForeground) return false
        if (lastTimeUsed != other.lastTimeUsed) return false
        if (describeContents != other.describeContents) return false
        if (appBytes != other.appBytes) return false
        if (dataBytes != other.dataBytes) return false
        if (cacheBytes != other.cacheBytes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packet?.hashCode() ?: 0
        result = 31 * result + totalTimeInForeground.hashCode()
        result = 31 * result + lastTimeUsed.hashCode()
        result = 31 * result + describeContents
        result = 31 * result + appBytes.hashCode()
        result = 31 * result + dataBytes.hashCode()
        result = 31 * result + cacheBytes.hashCode()
        return result
    }
}
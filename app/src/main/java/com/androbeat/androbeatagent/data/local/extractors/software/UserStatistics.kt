package com.androbeat.androbeatagent.data.local.extractors.software

import android.app.usage.StorageStatsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.os.Process
import android.os.storage.StorageManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.model.models.extractors.software.UserStatisticsModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.utils.ApplicationStatus
import java.util.UUID
import javax.inject.Inject

class UserStatistics @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
    private val storageManager: StorageManager,
    private val storageStatsManager: StorageStatsManager
) : DataExtractor, DataProvider<List<UserStatisticsModel?>?> {

    private val _stats = mutableListOf<UserStatisticsModel>()
    private val stats: List<UserStatisticsModel>
        get() = _stats
    private var startApp = 0L

    private fun getUserStatistics(startTime: Long, endTime: Long): String {
        val queryUsageStats =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime)
        createStatisticModel(queryUsageStats)
        val userAppStatsStringBuilder = generateStatLogStringBuilder()
        startApp = endTime + 1
        return userAppStatsStringBuilder.toString()
    }

    private fun getUserStadistics(): String {
        return getUserStatistics(startApp, System.currentTimeMillis())
    }

    private fun generateStatLogStringBuilder(): StringBuilder {
        val userAppStatsStringBuilder = StringBuilder("User apps statistics{")
        for (stat in stats) {
            userAppStatsStringBuilder.append("[")
                .append(stat.packet).append(" ")
                .append(stat.totalTimeInForeground).append(" ")
                .append(stat.lastTimeUsed).append(" ")
                .append(stat.describeContents).append(" ")
                .append(stat.appBytes).append(" ")
                .append(stat.dataBytes).append(" ")
                .append(stat.cacheBytes)
                .append("]")
        }
        userAppStatsStringBuilder.append("}")
        return userAppStatsStringBuilder
    }

    private fun createStatisticModel(queryUsageStats: List<UsageStats>) {
        _stats.clear()
        for (userStats in queryUsageStats) {
            var userStatisticsModel = UserStatisticsModel()
            userStatisticsModel = setPacketStatistics(userStatisticsModel, userStats)
            userStatisticsModel = setPacketStorageStatistics(userStatisticsModel, userStats)
            _stats.add(userStatisticsModel)
        }
    }

    private fun setPacketStatistics(
        userStatisticsModel: UserStatisticsModel,
        userStats: UsageStats
    ): UserStatisticsModel {
        userStatisticsModel.packet = userStats.packageName
        userStatisticsModel.totalTimeInForeground = userStats.totalTimeInForeground
        userStatisticsModel.lastTimeUsed = userStats.lastTimeStamp
        userStatisticsModel.describeContents = userStats.describeContents()
        return userStatisticsModel
    }

    private fun setPacketStorageStatistics(
        userStatisticsModel: UserStatisticsModel,
        userStats: UsageStats
    ): UserStatisticsModel {
        val storageVolume = storageManager.primaryStorageVolume
        val uuidStr = storageVolume.uuid
        val uuid = if (uuidStr == null) StorageManager.UUID_DEFAULT else UUID.fromString(uuidStr)
        val user = Process.myUserHandle()
        val packageName = userStats.packageName
        try {
            val storageStats = storageStatsManager.queryStatsForPackage(uuid, packageName, user)
            userStatisticsModel.appBytes = storageStats.appBytes
            userStatisticsModel.dataBytes = storageStats.dataBytes
            userStatisticsModel.cacheBytes = storageStats.cacheBytes
            ApplicationStatus.postOK()
        } catch (e: Exception) {
            e.printStackTrace()
            ApplicationStatus.postError("Error querying storage statistics: ${e.message}")
        }
        return userStatisticsModel
    }

    override val statistics: String
        get() = this.getUserStadistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.USER_STATISTICS
    override val data: List<UserStatisticsModel?>
        get() = stats
}
package com.androbeat.androbeatagent.data.local.extractors.network

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.NetworkCapabilities
import android.os.RemoteException
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.extractors.network.NetworkModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor

class NetworkStatistics(private val context: Context) : DataExtractor,
    DataProvider<List<NetworkModel?>?> {



    private val TRANSPORT_TYPE = NetworkCapabilities.TRANSPORT_WIFI
    private val EMPTY_SUBSCRIBER_ID = ""

    private var startApp = 0L
    private val _stats = mutableListOf<NetworkModel>()
    private val stats: List<NetworkModel>
        get() = _stats

    //This method is internal just for testing purposes
    internal fun getAppNetworkUsage(startTime: Long, endTime: Long): Map<String, LongArray> {
        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val networkStats: MutableMap<String, LongArray> = HashMap()
        try {
            networkStatsManager.querySummary(
                TRANSPORT_TYPE,
                EMPTY_SUBSCRIBER_ID,
                startTime,
                endTime
            ).use { networkStatsByApp ->
                val bucket = NetworkStats.Bucket()
                do {
                    networkStatsByApp.getNextBucket(bucket)
                    val packageName =
                        context.packageManager.getNameForUid(bucket.uid) ?: bucket.uid.toString()
                    val stats = networkStats.getOrPut(packageName) { longArrayOf(0, 0) }
                    stats[0] += bucket.rxBytes
                    stats[1] += bucket.txBytes
                } while (networkStatsByApp.hasNextBucket())
            }
        } catch (e: RemoteException) {
            Logger.logError(TAG, "Failed to query network stats $e")
        }
        startApp = endTime + 1
        return networkStats
    }

    private val appNetworkUsage: Map<String, LongArray>
        get() {
            _stats.clear()
            val networkUsage = getAppNetworkUsage(startApp, System.currentTimeMillis())
            for ((key, value) in networkUsage) {
                val networkModel = NetworkModel(key, value[0].toFloat(), value[1].toFloat())
                _stats.add(networkModel)
            }
            return networkUsage
        }

    fun getNetworkStatistics(): String {
        val appNetworkUsage = appNetworkUsage
        val stringBuilder = StringBuilder("Network{")
        for ((key, value) in appNetworkUsage) {
            stringBuilder.append("[")
                .append(key).append(" ")
                .append(value[0]).append(" ")
                .append(value[1])
                .append("]")
        }
        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    override val statistics: String
        get() = this.getNetworkStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.NETWORK_EXTRACTOR
    override val data: List<NetworkModel?>
        get() = stats

    companion object {
        private val TAG = NetworkStatistics::class.java.simpleName
    }

}
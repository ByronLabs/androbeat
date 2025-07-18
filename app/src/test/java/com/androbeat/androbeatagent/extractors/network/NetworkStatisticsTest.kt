package com.androbeat.androbeatagent.extractors.network

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.RemoteException
import com.androbeat.androbeatagent.data.local.extractors.network.NetworkStatistics
import com.androbeat.androbeatagent.data.logger.Logger
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkStatisticsTest {

    private lateinit var context: Context
    private lateinit var networkStatistics: NetworkStatistics
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        networkStatistics = NetworkStatistics(context)
    }

    @Test
    fun testGetAppNetworkUsage() {
        val networkStatsManager = mockk<NetworkStatsManager>()
        val networkStats = mockk<NetworkStats>()
        val bucket = mockk<NetworkStats.Bucket>()
        val packageManager = mockk<PackageManager>()

        every { context.getSystemService(Context.NETWORK_STATS_SERVICE) } returns networkStatsManager
        every { networkStatsManager.querySummary(any(), any(), any(), any()) } returns networkStats
        every { networkStats.getNextBucket(any()) } returns true andThen false
        every { networkStats.hasNextBucket() } returns true andThen false
        every { networkStats.close() } returns Unit

        every { context.packageManager } returns packageManager
        every { packageManager.getNameForUid(any()) } returns "com.example.app"

        val startTime = 0L
        val endTime = System.currentTimeMillis()
        val networkUsage = networkStatistics.getAppNetworkUsage(startTime, endTime)

        assert(networkUsage.containsKey("com.example.app"))
        assert(networkUsage["com.example.app"]?.get(0) == 0L)
        assert(networkUsage["com.example.app"]?.get(1) == 0L)
    }

    @Test
    fun testGetNetworkStatistics_handlesRemoteExceptionGracefully() {
        val networkStatsManager = mockk<NetworkStatsManager>()
        val packageManager = mockk<PackageManager>()

        every { networkStatsManager.querySummary(any(), any(), any(), any()) } returns mockk()
        every { context.getSystemService(Context.NETWORK_STATS_SERVICE) } returns networkStatsManager
        every { context.packageManager } returns packageManager
        every { packageManager.getNameForUid(any()) } returns "com.example.app"
        every { networkStatsManager.querySummary(any(), any(), any(), any()) } throws RemoteException()

        val statistics = networkStatistics.getNetworkStatistics()

        assertTrue(statistics.contains("Network{}") || statistics.contains("Error"))
    }
}
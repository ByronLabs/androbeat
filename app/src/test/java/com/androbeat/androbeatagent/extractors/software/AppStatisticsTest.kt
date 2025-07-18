package com.androbeat.androbeatagent.extractors.software

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.androbeat.androbeatagent.data.local.extractors.software.AppStatistics
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class AppStatisticsTest {

    @Test
    fun testGetAppStatistics() {
        // Mocking the Context and PackageManager classes
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val applicationInfo = mockk<ApplicationInfo>()

        // Defining the behavior of the mocked PackageManager
        every { context.packageManager } returns packageManager
        applicationInfo.packageName = "Test App"
        every { packageManager.getInstalledApplications(PackageManager.GET_META_DATA) } returns listOf(applicationInfo)

        // Creating an instance of AppStatistics and calling getAppStatistics
        val appStatistics = AppStatistics(context)
        val statistics = appStatistics.getAppStatistics()

        // Verifying the results
        assertTrue(statistics.contains("Apps{Test App"))
    }
}
package com.androbeat.androbeatagent.extractors.network

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.androbeat.androbeatagent.data.local.extractors.network.WifiList
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class WifiListTest {

    @Test
    fun testWifiBSSID() {
        // Mocking the Context and WifiManager classes
        val context = mockk<Context>()
        val wifiManager = mockk<WifiManager>()
        val scanResult = mockk<ScanResult>()

        // Defining the behavior of the mocked WifiManager
        every { context.getSystemService(Context.WIFI_SERVICE) } returns wifiManager
        every { context.checkPermission(any(), any(), any()) } returns PackageManager.PERMISSION_GRANTED
        scanResult.BSSID = "Test BSSID"
        every { wifiManager.scanResults } returns listOf(scanResult)

        // Creating an instance of WifiList and calling getWifiListStadistics
        val wifiList = WifiList(context)
        val statistics = wifiList.getWifiListStatistics()

        // Verifying the results
        assertTrue(statistics.contains("Test BSSID"))
    }

    @Test
    fun testWifiSSID() {
        // Mocking the Context and WifiManager classes
        val context = mockk<Context>()
        val wifiManager = mockk<WifiManager>()
        val scanResult = mockk<ScanResult>()

        // Defining the behavior of the mocked WifiManager
        every { context.getSystemService(Context.WIFI_SERVICE) } returns wifiManager
        every { context.checkPermission(any(), any(), any()) } returns PackageManager.PERMISSION_GRANTED
        scanResult.SSID = "Test SSID"
        every { wifiManager.scanResults } returns listOf(scanResult)

        // Creating an instance of WifiList and calling getWifiListStadistics
        val wifiList = WifiList(context)
        val statistics = wifiList.getWifiListStatistics()

        // Verifying the results
        assertTrue(statistics.contains("Test SSID"))
    }
}
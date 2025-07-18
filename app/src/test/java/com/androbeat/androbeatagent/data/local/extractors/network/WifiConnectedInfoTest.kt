package com.androbeat.androbeatagent.data.local.extractors.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WifiConnectedInfoTest {

    private lateinit var context: Context
    private lateinit var wifiConnectedInfo: WifiConnectedInfo
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var wifiInfo: WifiInfo
    private lateinit var network: Network
    private lateinit var networkCapabilities: NetworkCapabilities
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        context = mockk()
        connectivityManager = mockk()
        wifiManager = mockk()
        wifiInfo = mockk()
        network = mockk()
        networkCapabilities = mockk()
        logger = mockk(relaxed = true)
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { context.applicationContext.getSystemService(Context.WIFI_SERVICE) } returns wifiManager
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.transportInfo } returns wifiInfo

        wifiConnectedInfo = WifiConnectedInfo(context)

    }

    @Test
    fun testGetWifiStatistics_connected() {
        val wifiInfo = mockk<WifiInfo>()
        every { wifiInfo.ssid } returns "TestSSID"
        every { wifiInfo.bssid } returns "TestBSSID"

        val wifiManager = mockk<WifiManager>()
        every { wifiManager.connectionInfo } returns wifiInfo

        val connectivityManager = mockk<ConnectivityManager>()
        val network = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>()

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { context.applicationContext.getSystemService(Context.WIFI_SERVICE) } returns wifiManager
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.transportInfo } returns wifiInfo

        wifiConnectedInfo = WifiConnectedInfo(context)

        val result = wifiConnectedInfo.statistics

        assertEquals("Wifi connected {TestSSID TestBSSID}", result)
        assertEquals("TestSSID", wifiConnectedInfo.data.ssid)
        assertEquals("TestBSSID", wifiConnectedInfo.data.bssid)
    }

    @Test
    fun testGetWifiStatistics_notConnected() {
        every { connectivityManager.activeNetwork } returns null

        val result = wifiConnectedInfo.statistics

        assertEquals("No Wifi connection", result)
    }

    @Test
    fun testGetWifiStatistics_error() {
        every { connectivityManager.activeNetwork } throws RuntimeException()

        val result = wifiConnectedInfo.statistics

        assertEquals("Error retrieving Wifi information", result)
    }

    @Test
    fun testDataProviderType() {
        assertEquals(DataProviderType.CONNECTED_WIFI, wifiConnectedInfo.type)
    }

    @Test
    fun testDataProvider() {
        assertEquals(wifiConnectedInfo, wifiConnectedInfo.dataProvider)
    }
}
package com.androbeat.androbeatagent.extractors.software

import android.content.Context
import android.telephony.TelephonyManager
import com.androbeat.androbeatagent.data.local.extractors.software.BasicConfiguration
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import android.os.Build
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BasicConfigurationTest {

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var telephonyManager: TelephonyManager

    lateinit var basicConfiguration: BasicConfiguration


    @After
    fun tearDown() {
        // Close the static mock
        clearAllMocks()
    }
    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // Mockear TelephonyManager y Context
        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkOperatorName } returns "TestCarrier"
        every { telephonyManager.isNetworkRoaming } returns true

        // Inicializar la clase bajo prueba
        basicConfiguration = BasicConfiguration(context)
    }


    @Test
    fun testGetBasedConfigurationStatisticsUsesTelephonyManager() {
        val context = mockk<Context>()
        val telephonyManager = mockk<TelephonyManager>()

        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkOperatorName } returns "Test Operator"
        every { telephonyManager.isNetworkRoaming } returns false

        basicConfiguration = BasicConfiguration(context)
        val statistics = basicConfiguration.getBasedConfigurationStatistics()

        assertTrue(statistics.contains("Test Operator"))
        assertTrue(statistics.contains("false"))
    }

    @Test
    fun testGetBasedConfigurationStatisticsUsesBuild() {
        val context = mockk<Context>()
        val telephonyManager = mockk<TelephonyManager>()

        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkOperatorName } returns "Test Operator"
        every { telephonyManager.isNetworkRoaming } returns false

        val basicConfiguration = BasicConfiguration(context)
        val statistics = basicConfiguration.getBasedConfigurationStatistics()

        assertTrue(statistics.contains("Manufacturer: DEFAULT"))
        assertTrue(statistics.contains("Test Operator DEFAULT false"))
    }


    @Test
    fun testCheckIsNotEmulator() {

        val context = mockk<Context>(relaxed = true)
        val telephonyManager = mockk<TelephonyManager>(relaxed = true)
        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkOperatorName } returns "real_carrier_name"
        every { telephonyManager.isNetworkRoaming } returns false

        val basicConfiguration = BasicConfiguration(context)
        basicConfiguration.getBasedConfigurationStatistics()

        assertFalse(basicConfiguration.data.isEmulator)
    }


    @Test
    fun testNotNullOrEmpty() {
        val result = basicConfiguration.getBasedConfigurationStatistics()
        assertNotNull(result)
    }
}
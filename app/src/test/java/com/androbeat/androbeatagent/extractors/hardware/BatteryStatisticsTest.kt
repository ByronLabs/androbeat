package com.androbeat.androbeatagent.extractors.hardware

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.extractors.hardware.BatteryStatistics
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BatteryModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class BatteryStatisticsTest {

    private lateinit var context: Context
    private lateinit var batteryStatistics: BatteryStatistics
    private lateinit var logger: Logger

    @Before
    fun setup() {
        context = mockk()
        logger = mockk(relaxed = true)
        batteryStatistics = BatteryStatistics(context)
    }

    @Test
    fun testGetBatteryStatistics() {
        val mockIntent = mockk<Intent>()
        every { context.registerReceiver(null, any()) } returns mockIntent
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 50
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                -1
            )
        } returns BatteryManager.BATTERY_STATUS_CHARGING
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_PLUGGED,
                -1
            )
        } returns BatteryManager.BATTERY_PLUGGED_USB
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) } returns 4000
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) } returns 300
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_HEALTH,
                -1
            )
        } returns BatteryManager.BATTERY_HEALTH_UNKNOWN

        val expectedBatteryData = "Battery{true USB ${"%.4f".format(Locale.getDefault(), 50f)} 4000mV 30.0°C Unknown}"
        val actualBatteryData = batteryStatistics.getBatteryStatistics()

        assertEquals(expectedBatteryData, actualBatteryData)
    }

    @Test
    fun testStatisticsProperty() {
        val mockIntent = mockk<Intent>()
        every { context.registerReceiver(null, any()) } returns mockIntent
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 50
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                -1
            )
        } returns BatteryManager.BATTERY_STATUS_CHARGING
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_PLUGGED,
                -1
            )
        } returns BatteryManager.BATTERY_PLUGGED_USB
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) } returns 4000
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) } returns 300
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_HEALTH,
                -1
            )
        } returns BatteryManager.BATTERY_HEALTH_UNKNOWN

        val expectedStatistics = "Battery{true USB ${"%.4f".format(Locale.getDefault(), 50f)} 4000mV 30.0°C Unknown}"
        assertEquals(expectedStatistics, batteryStatistics.statistics)
    }

    @Test
    fun testDataProviderProperty() {
        assertEquals(batteryStatistics, batteryStatistics.dataProvider)
    }

    @Test
    fun testTypeProperty() {
        assertEquals(DataProviderType.BATTERY_EXTRACTOR, batteryStatistics.type)
    }

    @Test
    fun testDataProperty() {
        val expectedBatteryModel = BatteryModel().apply {
            percentage = 50.0f
            isCharging = true
            chargingMode = "USB"
            voltage = 4000
            temperature = 300
            health = "Unknown"
        }

        val mockIntent = mockk<Intent>()
        every { context.registerReceiver(null, any()) } returns mockIntent
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 50
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                -1
            )
        } returns BatteryManager.BATTERY_STATUS_CHARGING
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_PLUGGED,
                -1
            )
        } returns BatteryManager.BATTERY_PLUGGED_USB
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) } returns 4000
        every { mockIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) } returns 300
        every {
            mockIntent.getIntExtra(
                BatteryManager.EXTRA_HEALTH,
                -1
            )
        } returns BatteryManager.BATTERY_HEALTH_UNKNOWN

        batteryStatistics.getBatteryStatistics()
        assertEquals(expectedBatteryModel, batteryStatistics.data)
    }

}

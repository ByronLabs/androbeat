package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.HumiditySensor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HumiditySensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var humiditySensor: HumiditySensor

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        humiditySensor = HumiditySensor(context, 12)
    }

    @Test
    fun testSensorType() {
        val humiditySensor = HumiditySensor(context, 12)
        assertEquals(humiditySensor.type.name, DataProviderType.HUMIDITY_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val humiditySensor = HumiditySensor(context, 12)
        val expectedData = 0.0f
        assertEquals(expectedData, humiditySensor.data)
    }

    @Test
    fun onSensorValuesChangedShouldUpdateHumidityModelAndLogValue() {
        val testValues = floatArrayOf(57.25f)
        val listener = humiditySensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)
        assertEquals(57.25f, humiditySensor.humiditySensorModel, 0.0001f)
    }
}
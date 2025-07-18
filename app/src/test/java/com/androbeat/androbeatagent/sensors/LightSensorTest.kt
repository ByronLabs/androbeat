package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.local.sensors.LightSensor
import com.androbeat.androbeatagent.data.enums.DataProviderType
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LightSensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: LightSensor

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)

        // Simula el comportamiento de getSystemService para devolver el sensorManager simulado
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager

        lightSensor = LightSensor(context,5)
    }

    @Test
    fun testSensorType() {
        val lightSensor = LightSensor(context, 5)
        assertEquals(lightSensor.type.name, DataProviderType.LIGHT_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val lightSensor = LightSensor(context, 5)
        val expectedData = 0.0f
        assertEquals(expectedData, lightSensor.data)
    }

    @Test
    fun onSensorValuesChangedWithNullShouldSetLightModelToZero() {
        val listener = lightSensor.getOnSensorValuesChangedListener()
        listener?.accept(null)
        assertEquals(0f, lightSensor.lightSensorModel)
    }

}
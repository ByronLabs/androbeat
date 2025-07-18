package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.PressureSensor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PressureSensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: PressureSensor

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)

        // Simula el comportamiento de getSystemService para devolver el sensorManager simulado
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager

        pressureSensor = PressureSensor(context, 6)
    }

    @Test
    fun testSensorType() {
        val pressureSensor = PressureSensor(context, 6)
        assertEquals(pressureSensor.type.name, DataProviderType.PRESSURE_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val pressureSensor = PressureSensor(context, 6)
        val expectedData = 0.0f
        assertEquals(expectedData, pressureSensor.data)
    }

    @Test
    fun onSensorValuesChangedListenerShouldSetPressureModel() {
        val testValues = floatArrayOf(1013.25f)
        val listener = pressureSensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)
        assertEquals(1013.25f, pressureSensor.pressureSensorModel, 0.0001f)
    }
}
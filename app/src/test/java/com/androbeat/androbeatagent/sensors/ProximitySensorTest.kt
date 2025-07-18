package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.ProximitySensor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProximitySensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var proximitySensor: ProximitySensor

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        proximitySensor = ProximitySensor(context, 8)
    }

    @Test
    fun testSensorType() {
        val proximitySensor = ProximitySensor(context, 8)
        assertEquals(proximitySensor.type.name, DataProviderType.PROXIMITY_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val proximitySensor = ProximitySensor(context, 8)
        val expectedData = false
        assertEquals(expectedData, proximitySensor.data)
    }

    @Test
    fun onSensorValuesChangedShouldSetProximityFalseWhenValueIsNonZero() {
        val listener = proximitySensor.getOnSensorValuesChangedListener()
        listener?.accept(floatArrayOf(5f))
        assertEquals(false, proximitySensor.proximitySensorModel)
    }
}
package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.local.sensors.GyroscopeSensor
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.data.enums.DataProviderType
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test

class GyroscopeSensorTest {

    private lateinit var mockGyroscopeSensor: AxisSensorModel
    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscopeSensor: GyroscopeSensor

    @Before
    fun setup() {
        mockGyroscopeSensor = mockk(relaxed = true)
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)

        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        every { sensorManager.getDefaultSensor(any()) } returns mockk()

        gyroscopeSensor = GyroscopeSensor(context,4)
    }

    @Test
    fun testSensorType() {
        val gyroscopeSensor = GyroscopeSensor(context, 4)
        assertEquals(gyroscopeSensor.type.name, DataProviderType.GYROSCOPE_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val gyroscopeSensor = GyroscopeSensor(context, 4)
        val expectedData = AxisSensorModel(0.0F, 0.0F, 0.0f)
        gyroscopeSensor.gyroscopeSensorModel = mockGyroscopeSensor
        assertEquals(expectedData.x, gyroscopeSensor.data.x)
        assertEquals(expectedData.y, gyroscopeSensor.data.y)
        assertEquals(expectedData.z, gyroscopeSensor.data.z)
    }

    @Test
    fun onSensorValuesChangedShouldUpdateGyroscopeModelAndLogValues() {
        val testValues = floatArrayOf(0.5f, 1.5f, -0.5f)

        val listener = gyroscopeSensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)

        val model = gyroscopeSensor.gyroscopeSensorModel
        assertEquals(0.5f, model.x, 0.0001f)
        assertEquals(1.5f, model.y, 0.0001f)
        assertEquals(-0.5f, model.z, 0.0001f)
    }
}
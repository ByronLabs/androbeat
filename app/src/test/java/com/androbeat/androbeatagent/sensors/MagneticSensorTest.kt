package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.MagneticSensor
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MagneticSensorTest {

    private lateinit var mockMagneticSensorModel: AxisSensorModel
    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var mockLogger: Logger
    private lateinit var sensor: MagneticSensor

    @Before
    fun setup() {
        mockMagneticSensorModel = mockk(relaxed = true)
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)

        mockLogger = mockk(relaxed = true)
        mockkObject(Logger)

        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
    }

    @Test
    fun testSensorType() {
        val magneticSensor = MagneticSensor(context, 2)
        assertEquals(magneticSensor.type.name, DataProviderType.MAGNETIC_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val magneticSensor = MagneticSensor(context, 2)
        val expectedData = AxisSensorModel(0.0F, 0.0F, 0.0f)
        magneticSensor.magenticFieldSensorModel = mockMagneticSensorModel
        assertEquals(expectedData.x, magneticSensor.data.x)
        assertEquals(expectedData.y, magneticSensor.data.y)
        assertEquals(expectedData.z, magneticSensor.data.z)
    }

    @Test
    fun testOnSensorValuesChangedListener() {
        sensor = MagneticSensor(context, 2)
        val testValues = floatArrayOf(1.0f, 2.0f, 3.0f)
        val consumer = sensor.getOnSensorValuesChangedListener()
        consumer?.accept(testValues)
        assertEquals(1.0f, sensor.magenticFieldSensorModel.x, 0.0001f)
        assertEquals(2.0f, sensor.magenticFieldSensorModel.y, 0.0001f)
        assertEquals(3.0f, sensor.magenticFieldSensorModel.z, 0.0001f)
    }

}
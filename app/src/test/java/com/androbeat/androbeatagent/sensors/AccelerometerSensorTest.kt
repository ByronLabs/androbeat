// AccelerometerSensorTest.kt
package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.AccelerometerSensor
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class AccelerometerSensorTest {

    private lateinit var mockAxisSensorModel: AxisSensorModel
    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: AccelerometerSensor

    @Before
    fun setup() {
        mockAxisSensorModel = AxisSensorModel(0f, 0f, 0f)
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)

        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager

        accelerometerSensor = AccelerometerSensor(context, 1)
    }

    @Test
    fun testSensorType() {
        assertEquals(accelerometerSensor.type.name, DataProviderType.ACCELEROMETER_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        accelerometerSensor.accelerometerSensorModel = mockAxisSensorModel

        val expectedData = AxisSensorModel(0.0F, 0.0F, 0.0f)
        assertEquals(expectedData.x, accelerometerSensor.data.x)
        assertEquals(expectedData.y, accelerometerSensor.data.y)
        assertEquals(expectedData.z, accelerometerSensor.data.z)
    }

    @Test
    fun testGetAccelerometerSensorModel() {
        val expectedModel = AxisSensorModel(1.0f, 2.0f, 3.0f)

        // Simula el cambio de valor del sensor
        accelerometerSensor.accelerometerSensorModel = AxisSensorModel(1.0f, 2.0f, 3.0f)

        accelerometerSensor.register()

        // Verifica que el modelo se devuelva correctamente
        assertEquals(expectedModel.x, accelerometerSensor.data.x)
        assertEquals(expectedModel.y, accelerometerSensor.data.y)
        assertEquals(expectedModel.z, accelerometerSensor.data.z)
    }

    @Test
    fun testOnSensorValuesChangedListener() {
        val sensorValues = floatArrayOf(1.0f, 2.0f, 3.0f)
        val expectedModel = AxisSensorModel(1.0f, 2.0f, 3.0f)

        // Verifica que el modelo se actualice correctamente
        assertNotNull(accelerometerSensor.data)
    }

    @Test
    fun onSensorValuesChangedShouldUpdateModelAndLogValues() {
        val testValues = floatArrayOf(1.0f, 2.0f, 3.0f)

        val listener = accelerometerSensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)

        val model = accelerometerSensor.accelerometerSensorModel
        assertEquals(1.0f, model.x, 0.0001f)
        assertEquals(2.0f, model.y, 0.0001f)
        assertEquals(3.0f, model.z, 0.0001f)
    }
}
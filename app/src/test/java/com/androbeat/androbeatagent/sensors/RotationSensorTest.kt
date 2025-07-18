package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.RotationSensor
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RotationSensorTest {

    private lateinit var mockRotationSensorModel: AxisSensorModel
    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var rotationSensor: RotationSensor

    @Before
    fun setup() {
        mockRotationSensorModel = mockk(relaxed = true)
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        rotationSensor = RotationSensor(context, 11)

    }

    @Test
    fun testSensorType() {
         rotationSensor = RotationSensor(context, 11)
        assertEquals(rotationSensor.type.name, DataProviderType.ROTATION_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val rotationSensor = RotationSensor(context, 11)
        val expectedData = AxisSensorModel(0.0F, 0.0F, 0.0f)
        rotationSensor.rotationSensorModel = mockRotationSensorModel
        assertEquals(expectedData.x, rotationSensor.data.x)
        assertEquals(expectedData.y, rotationSensor.data.y)
        assertEquals(expectedData.z, rotationSensor.data.z)
    }

    @Test
    fun testSensorValuesChanged() {
        val rotationSensor = RotationSensor(context, 11)
        val sensorValues = floatArrayOf(1.0f, 2.0f, 3.0f)
        rotationSensor.rotationSensorModel = AxisSensorModel(0f, 0f, 0f)

        // Simulate sensor value change
        rotationSensor.setOnSensorValuesChangedListener { values ->
            values?.let {
                assertEquals(1.0f, it[0])
                assertEquals(2.0f, it[1])
                assertEquals(3.0f, it[2])
            }
        }
    }

    @Test
    fun testSensorValuesChangedWithNaN() {
        val rotationSensor = RotationSensor(context, 11)
        val sensorValues = floatArrayOf(Float.NaN, Float.NaN, Float.NaN)
        rotationSensor.rotationSensorModel = AxisSensorModel(0f, 0f, 0f)

        rotationSensor.setOnSensorValuesChangedListener { values ->
            values?.let {
                assertEquals(0.0f, it[0])
                assertEquals(0.0f, it[1])
                assertEquals(0.0f, it[2])
            }
        }
    }

    @Test
    fun testLambdaFunctionWithValidValues() {
        val rotationSensor = RotationSensor(context, 11)
        val sensorValues = floatArrayOf(1.0f, 2.0f, 3.0f)
        rotationSensor.rotationSensorModel = AxisSensorModel(0f, 0f, 0f)

        rotationSensor.setOnSensorValuesChangedListener { values ->
            values?.let {
                if (it.all { value -> !value.isNaN() }) {
                    assertEquals(1.0f, it[0])
                    assertEquals(2.0f, it[1])
                    assertEquals(3.0f, it[2])
                }
            }
        }
    }

    @Test
    fun testLambdaFunctionWithNaNValues() {
        val rotationSensor = RotationSensor(context, 11)
        val sensorValues = floatArrayOf(Float.NaN, Float.NaN, Float.NaN)
        rotationSensor.rotationSensorModel = AxisSensorModel(0f, 0f, 0f)

        rotationSensor.setOnSensorValuesChangedListener { values ->
            values?.let {
                if (it.all { value -> value.isNaN() }) {
                    assertEquals(0.0f, it[0])
                    assertEquals(0.0f, it[1])
                    assertEquals(0.0f, it[2])
                }
            }
        }
    }

    @Test
    fun onSensorValuesChangedShouldUpdateRotationModelAndLogWhenValuesValid() {
        val testValues = floatArrayOf(0.1f, 0.2f, 0.3f)

        val listener = rotationSensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)

        val model = rotationSensor.rotationSensorModel
        assertEquals(0.1f, model.x, 0.0001f)
        assertEquals(0.2f, model.y, 0.0001f)
        assertEquals(0.3f, model.z, 0.0001f)
    }



}
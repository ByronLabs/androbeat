package com.androbeat.androbeatagent.sensors

import android.content.Context
import android.hardware.SensorManager
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.local.sensors.StepsSensor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StepsSensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var stepsSensor: StepsSensor

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        stepsSensor = StepsSensor(context, 19)
    }

    @Test
    fun testSensorType() {
        val stepsSensor = StepsSensor(context, 19)
        assertEquals(stepsSensor.type.name, DataProviderType.STEPS_SENSOR.name)
    }

    @Test
    fun testStandardData() {
        val stepsSensor = StepsSensor(context, 19)
        val expectedData = 0.0f
        assertEquals(expectedData, stepsSensor.data)
    }

    @Test
    fun onSensorValuesChangedShouldUpdateStepsModelAndLogValue() {
        val testValues = floatArrayOf(1024.0f)
        val listener = stepsSensor.getOnSensorValuesChangedListener()
        listener?.accept(testValues)

        assertEquals(1024.0f, stepsSensor.stepsSensorModel, 0.0001f)
    }
}
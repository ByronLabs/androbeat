package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.function.Consumer

class AndroidSensorTest {

    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var androidSensor: AndroidSensor

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        sensor = mockk(relaxed = true)
        androidSensor = spyk(AndroidSensor(context, SensorManager.SENSOR_ACCELEROMETER.toString(), Sensor.TYPE_ACCELEROMETER))

        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
        every { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns sensor
    }

    @Test
    fun registerShouldRegisterSensorListenerWhenSensorIsAvailable() {
        every { context.packageManager.hasSystemFeature(SensorManager.SENSOR_ACCELEROMETER.toString()) } returns true
        androidSensor.register()
        verify { sensorManager.registerListener(androidSensor, sensor, Int.MAX_VALUE, Int.MAX_VALUE) }
    }


    @Test
    fun onSensorChangedShouldCallListenerWhenSensorIsAvailable() {
        every { context.packageManager.hasSystemFeature(SensorManager.SENSOR_ACCELEROMETER.toString()) } returns true
        val listener = mockk<Consumer<FloatArray?>>(relaxed = true)
        androidSensor.setOnSensorValuesChangedListener(listener)
        val event = mockk<SensorEvent>(relaxed = true)
        androidSensor.onSensorChanged(event)
        verify { listener.accept(event.values) }
    }

    @Test
    fun unregisterShouldUnregisterSensorListenerWhenSensorIsAvailable() {
        every { context.packageManager.hasSystemFeature(SensorManager.SENSOR_ACCELEROMETER.toString()) } returns true
        androidSensor.register()
        androidSensor.unregister()
        verify { sensorManager.unregisterListener(androidSensor) }
    }

    @Test
    fun powerShouldReturnSensorPowerWhenSensorIsAvailable() {
        every { context.packageManager.hasSystemFeature(SensorManager.SENSOR_ACCELEROMETER.toString()) } returns true
        every { sensor.power } returns 1.0f
        androidSensor.register()
        val power = androidSensor.power
        assertEquals(1.0f, power, 0.0f)
    }

    @Test
    fun isWakeUpSensorShouldReturnTrueWhenSensorIsWakeUpSensor() {
        every { context.packageManager.hasSystemFeature(SensorManager.SENSOR_ACCELEROMETER.toString()) } returns true
        every { sensor.isWakeUpSensor } returns true
        androidSensor.register()
        val isWakeUpSensor = androidSensor.isWakeUpSensor
        assertTrue(isWakeUpSensor)
    }

}
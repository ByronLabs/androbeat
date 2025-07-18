// SensorManagerTest.kt
package com.androbeat.androbeatagent.data.repository.managers

import android.content.Context
import com.androbeat.androbeatagent.data.local.sensors.AndroidSensor
import com.androbeat.androbeatagent.data.local.sensors.SensorFactory
import com.androbeat.androbeatagent.data.logger.Logger
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class SensorManagerTest {

    private lateinit var sensorManager: SensorManager
    private lateinit var context: Context
    private lateinit var sensorFactory: SensorFactory
    private lateinit var logger: Logger
    @Before
    fun setUp() {
        context = mockk()
        sensorFactory = mockk()
        sensorManager = SensorManager(context, sensorFactory)
        logger = mockk(relaxed = true)
    }

    @Test
    fun testUnregisterSensors() {
        val androidSensor = mockk<AndroidSensor>(relaxed = true)
        sensorManager.addSensor(androidSensor) // Use the new method to add sensor
        sensorManager.unregisterSensors()
        verify { androidSensor.unregister() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
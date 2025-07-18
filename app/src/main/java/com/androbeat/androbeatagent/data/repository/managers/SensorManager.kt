// SensorManager.kt
package com.androbeat.androbeatagent.data.repository.managers

import android.content.Context
import com.androbeat.androbeatagent.data.definitions.SensorsDefinitions
import com.androbeat.androbeatagent.data.local.sensors.AndroidSensor
import com.androbeat.androbeatagent.data.local.sensors.SensorFactory
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.data.ISensorManager
import javax.inject.Inject

class SensorManager @Inject constructor(
    private val context: Context,
    private val sensorFactory: SensorFactory,
) : ISensorManager {
    private val _sensors = mutableListOf<AndroidSensor>()
    private val sensorTypes = SensorsDefinitions.sensorTypes

    val sensors: List<AndroidSensor>
        get() = _sensors

    override fun createSensors() {
        for (sensorType in sensorTypes) {
            val createdSensor = sensorFactory.createSensor(context, sensorType)
            if (createdSensor != null) {
                _sensors.add(createdSensor)
                Logger.logInfo("SensorManager", "Sensor created: $sensorType (${createdSensor.javaClass.simpleName})")
            } else {
                Logger.logError("SensorManager", "Failed to create sensor: $sensorType")
            }
        }
    }

    override fun unregisterSensors() {
        for (sensor in sensors) {
            sensor.unregister()
            Logger.logInfo("SensorManager", "Sensor unregistered: $sensor")
        }
    }

    override fun getProviders(): List<DataProvider<*>> {
        return sensors.map { it as DataProvider<*> }.toSet().toList()
    }

    override fun getSensorNames(): List<String> {
        return sensors.map { it.javaClass.simpleName }
    }

    // Method to add sensors for testing purposes
    fun addSensor(sensor: AndroidSensor) {
        _sensors.add(sensor)
    }
}
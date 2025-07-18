package com.androbeat.androbeatagent.domain.sensors

import java.util.function.Consumer

interface IAndroidSensor {
    fun register()
    fun unregister()
    val power: Float
    val isWakeUpSensor: Boolean
    fun setOnSensorValuesChangedListener(listener: Consumer<FloatArray?>?)
}
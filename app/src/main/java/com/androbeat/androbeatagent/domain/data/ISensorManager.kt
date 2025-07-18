package com.androbeat.androbeatagent.domain.data

interface ISensorManager {
    fun createSensors()
    fun unregisterSensors()
    fun getProviders(): List<DataProvider<*>>
    fun getSensorNames(): List<String>
}
package com.androbeat.androbeatagent.domain.data

interface IDataProcessingManager {
    fun startDataProcessingCoroutine()
    fun createExtractors()
    fun createSensors()
    fun registerProviders()
    fun unregisterSensors()
}
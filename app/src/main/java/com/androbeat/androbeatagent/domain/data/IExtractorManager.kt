package com.androbeat.androbeatagent.domain.data

interface IExtractorManager {
    fun createExtractors()
    fun getProviders(): List<DataProvider<*>>
    fun getExtractorNames(): List<String>
}
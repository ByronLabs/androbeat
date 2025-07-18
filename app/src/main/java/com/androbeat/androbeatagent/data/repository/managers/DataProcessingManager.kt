package com.androbeat.androbeatagent.data.repository.managers

import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.IDataProcessingManager
import com.androbeat.androbeatagent.utils.ApplicationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataProcessingManager @Inject constructor(
    private val dataManager: DataManagerImp,
    private val extractorManager: ExtractorManager,
    private val sensorManager: SensorManager
) : IDataProcessingManager {
    companion object {
        private val TAG = DataProcessingManager::class.java.simpleName
        private const val DELAY = 5 * 1000
    }
    
    private var dataProcessingJob: Job? = null

    override fun startDataProcessingCoroutine() {
        dataProcessingJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                ApplicationStatus.postOK()
                while (isActive) {
                    extractDataFromExtractors()
                    dataManager.saveDataOnElasticSearch()
                    delay(DELAY.toLong())
                }
            } catch (e: Exception) {
                ApplicationStatus.postError("Error in data processing coroutine: ${e.message}")
                Logger.logError(TAG, "Error in data processing coroutine $e")
            }
        }
    }

    fun stopDataProcessingCoroutine() {
        dataProcessingJob?.cancel()
    }

    private fun extractDataFromExtractors() {
        for (extractor in extractorManager.extractors) {
            try {
                val stats = extractor.statistics
                Logger.logInfo(TAG, "✓ Extractor ${extractor.javaClass.simpleName} ran successfully")
            } catch (e: Exception) {
                Logger.logError(TAG, "✗ Extractor ${extractor.javaClass.simpleName} failed: ${e.message}")
            }
        }
    }

    override fun createExtractors() {
        extractorManager.createExtractors()
    }

    override fun createSensors() {
        sensorManager.createSensors()
    }

    override fun registerProviders() {
        try {
            dataManager._providers.clear()

            val extractorProviders = extractorManager.getProviders()
            val sensorProviders = sensorManager.getProviders()

            dataManager._providers.addAll(extractorProviders)
            dataManager._providers.addAll(sensorProviders)

            Logger.logInfo(TAG, "Providers registered: ${dataManager._providers.size}")
        } catch (e: Exception) {
            Logger.logError(TAG, "Error extracting data $e")
        }
    }

    override fun unregisterSensors() {
        sensorManager.unregisterSensors()
    }
}
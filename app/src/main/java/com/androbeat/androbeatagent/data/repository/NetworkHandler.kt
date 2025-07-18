package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import com.androbeat.androbeatagent.utils.ApplicationStatus
import javax.inject.Inject

class NetworkHandler @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dataManager: DataManagerImp
) : INetworkHandler {
    override fun processPendingData() {
        try {
            var processed = 0
            val allData = appDatabase.elasticDataDao().all
            allData?.let {
                for (data in it) {
                    if (Thread.currentThread().isInterrupted) {
                        return
                    }
                    data?.let { dataManager.saveDataOnElasticSearch(it, true) }
                    processed++
                }
            }
            ApplicationStatus.postOK()

        } catch (e: Exception) {
            ApplicationStatus.postError("Error processing pending data: ${e.message}")
        }
    }
}
package com.androbeat.androbeatagent.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.androbeat.androbeatagent.data.repository.NetworkHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ElasticFlushWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val networkHandler: NetworkHandler
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            networkHandler.processPendingData()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}

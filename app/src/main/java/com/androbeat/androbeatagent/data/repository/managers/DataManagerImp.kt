package com.androbeat.androbeatagent.data.repository.managers

import android.accounts.AccountManager
import android.content.Context
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.dto.ElasticDataRequest
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModelBuilder
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.domain.data.DataManager
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.utils.ApplicationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Named

class DataManagerImp @Inject constructor(
    private val context: Context,
    @Named("RoomExecutor") private val roomExecutor: ExecutorService,
    private val logstashApiInterface: LogstashApiInterface,
    private val appDatabase: AppDatabase,
    @Named("DateFormatter") private val dateFormatter: SimpleDateFormat
) : DataManager {

    companion object {
        private const val TAG = "DataManagerImp"
        private const val NO_ACCOUNT_FOUND = "no-account-found"
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)

    val currentDateInDesiredFormat: String
        get() = dateFormatter.format(Date())

    var _providers: MutableList<DataProvider<*>> = mutableListOf()

    override fun saveDataOnElasticSearch(data: ElasticDataModel, isFromCache: Boolean) {
        val elasticIndex = "androbeat.events.$currentDateInDesiredFormat"
        makeSaveDataOnLogstashCall(data, elasticIndex, isFromCache)
    }

    override fun saveDataOnElasticSearch() {
        val data = ElasticDataModelBuilder().fromDataProviders(_providers)
        val elasticIndex = "androbeat.events.$currentDateInDesiredFormat"
        val filledData = runBlocking { fillIdentificationInfo(context, data) }
        makeSaveDataOnLogstashCall(filledData, elasticIndex, false)
    }

    private suspend fun getDeviceIdFromRoom(): String? = withContext(Dispatchers.IO) {
        appDatabase.deviceIdDao().getDeviceId()?.deviceId
    }

    private suspend fun getClientIdFromRoom(): String? = withContext(Dispatchers.IO) {
        appDatabase.clientIdDao().getClientId()?.name
    }

    private suspend fun fillIdentificationInfo(
        context: Context,
        data: ElasticDataModel
    ): ElasticDataModel {
        val deviceId = runBlocking { getDeviceIdFromRoom() } ?: getDeviceIdFromRoom()
        val clientId = runBlocking { getClientIdFromRoom() } ?: getClientIdFromRoom()
        val mainAccountName = getMainAccountName(context)

        data.basicConfiguration?.apply {
            if (deviceId != null) {
                this.deviceId = deviceId
            }
            this.mainAccountName = mainAccountName
            if (clientId != null) {
                this.clientId = clientId
            }
        }

        Logger.logInfo(
            TAG,
            "Filled Data: Device ID: $deviceId, Main Account Name: $mainAccountName, Client ID: $clientId"
        )
        return data
    }

    private fun getMainAccountName(context: Context): String {
        ApplicationStatus.postOK()
        return try {
            val accountManager = AccountManager.get(context)
            val accounts = accountManager.accounts
            if (accounts.isNotEmpty()) {
                val mainAccount = accounts[0]
                Logger.logInfo(
                    TAG,
                    "Main Account Name: ${mainAccount.name}, Type: ${mainAccount.type}"
                )
                mainAccount.name
            } else {
                Logger.logInfo(TAG, "No accounts found.")
                NO_ACCOUNT_FOUND
            }
        } catch (e: Exception) {
            Logger.logError(TAG, "Error while getting accounts $e")
            ApplicationStatus.postError("Error getting accounts: ${e.message}")
            NO_ACCOUNT_FOUND
        }
    }

    private fun makeSaveDataOnLogstashCall(
        data: ElasticDataModel,
        elasticIndex: String,
        isFromCache: Boolean
    ) {
        ioScope.launch {
            try {
                val response = logstashApiInterface.sendToLogstash(ElasticDataRequest(elasticIndex, data))
                if (response.isSuccessful) {
                    Logger.logDebug(TAG, "Logstash upload success. Removing from DB...")
                    roomExecutor.execute { appDatabase.elasticDataDao().delete(data) }
                } else {
                    Logger.logError(TAG, "Logstash upload failed (code=${response.code()})")
                    handleFailure(data, isFromCache)
                }
            } catch (e: Exception) {
                Logger.logError(TAG, "Logstash upload exception: ${e.message}")
                handleFailure(data, isFromCache)
            }
        }
    }

    private fun handleFailure(data: ElasticDataModel, isFromCache: Boolean) {
        if (!isFromCache) {
            Logger.logDebug(TAG, "Failure. Adding to DB...")
            roomExecutor.execute { appDatabase.elasticDataDao().insert(data) }
        }
    }
}

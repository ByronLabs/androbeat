// DataManagerImp.kt
package com.androbeat.androbeatagent.data.repository.managers

import android.accounts.AccountManager
import android.content.Context
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModelBuilder
import com.androbeat.androbeatagent.data.model.models.elastic.ResponseData
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.domain.data.DataManager
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.elastic.ElasticApiInterface
import com.androbeat.androbeatagent.utils.ApplicationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Named

class DataManagerImp @Inject constructor(
    private val context: Context,
    @Named("RoomExecutor") private val roomExecutor: ExecutorService,
    private val apiInterface: ElasticApiInterface,
    private val appDatabase: AppDatabase,
    @Named("DateFormatter") private val dateFormatter: SimpleDateFormat
) : DataManager {


    companion object {
        private const val TAG = "DataManagerImp"
        private const val NO_ACCOUNT_FOUND = "no-account-found"
    }

    val currentDateInDesiredFormat: String
        get() = dateFormatter.format(Date())

    var _providers: MutableList<DataProvider<*>> = mutableListOf()

    override fun saveDataOnElasticSearch(data: ElasticDataModel, isFromCache: Boolean) {
        val elasticIndex = "androbeat.events.$currentDateInDesiredFormat"
        makeSaveDataOnElasticCall(data, elasticIndex, isFromCache)
    }

    override fun saveDataOnElasticSearch() {
        val data: ElasticDataModel = ElasticDataModelBuilder().fromDataProviders(_providers)
        val elasticIndex = "androbeat.events.$currentDateInDesiredFormat"
        val filledData = runBlocking { fillIdentificationInfo(context, data) }
        makeSaveDataOnElasticCall(filledData, elasticIndex, false)
    }

    private suspend fun getDeviceIdFromRoom(): String? {
        return withContext(Dispatchers.IO) {
            appDatabase.deviceIdDao().getDeviceId()?.deviceId
        }
    }

    private suspend fun getClientIdFromRoom(): String? {
        return withContext(Dispatchers.IO) {
            appDatabase.clientIdDao().getClientId()?.name
        }
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

    private fun makeSaveDataOnElasticCall(
        data: ElasticDataModel, elasticIndex: String, isFromCache: Boolean
    ) {
        try {
            val checkIndexCall = apiInterface.checkIndexExists(elasticIndex)
            checkIndexCall?.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        saveDataToElastic(elasticIndex, data, isFromCache)
                    } else if (response.code() == 404) {
                        createIndexAndRetry(elasticIndex, data, isFromCache)
                    } else {
                        Logger.logError(TAG, "Failed to check index existence: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    handleFailure(t, data, isFromCache)
                }
            })
        } catch (e: Exception) {
            handleFailure(data, isFromCache)
            Logger.logError(TAG, "Error making save data on ElasticSearch call $e")
        }
    }

    private fun saveDataToElastic(
        elasticIndex: String,
        data: ElasticDataModel,
        isFromCache: Boolean
    ) {
        val call = apiInterface.saveData(elasticIndex, data)
        call?.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                handleResponse(response, data, isFromCache)
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                handleFailure(t, data, isFromCache)
            }
        })
    }

    private fun createIndexAndRetry(
        elasticIndex: String,
        data: ElasticDataModel,
        isFromCache: Boolean
    ) {
        val createIndexCall = apiInterface.createIndex(elasticIndex)
        createIndexCall?.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (response.isSuccessful) {
                    makeSaveDataOnElasticCall(data, elasticIndex, isFromCache)
                } else {
                    Logger.logError(TAG, "Failed to create index: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Logger.logError(TAG, "Error creating index $t")
            }
        })
    }

    private fun handleResponse(
        response: Response<ResponseData?>, data: ElasticDataModel, isFromCache: Boolean
    ) {
        Logger.logDebug(TAG, "Response code: ${response.code()}")
        if (response.isSuccessful) {
            Logger.logDebug(TAG, "Success. Removing from DB...")
            roomExecutor.execute { appDatabase.elasticDataDao().delete(data) }
        } else if (!isFromCache) {
            Logger.logDebug(TAG, "Not success. Adding to DB...")
            roomExecutor.execute { appDatabase.elasticDataDao().insert(data) }
        }
    }

    private fun handleFailure(t: Throwable, data: ElasticDataModel, isFromCache: Boolean) {
        if (!isFromCache) {
            Logger.logDebug(TAG, "Failure. Adding to DB... $t")
            roomExecutor.execute { appDatabase.elasticDataDao().insert(data) }
        }
    }

    private fun handleFailure(data: ElasticDataModel, isFromCache: Boolean) {
        if (!isFromCache) {
            Logger.logDebug(TAG, "Failure. Adding to DB...")
            roomExecutor.execute { appDatabase.elasticDataDao().insert(data) }
        }
    }

}
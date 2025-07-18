package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.model.models.Client

class ClientRepository(private val appDatabase: AppDatabase) {
    suspend fun getClientId(): Client? {
        return appDatabase.clientIdDao().getClientId()
    }

    suspend fun saveClientId(client: Client) {
        appDatabase.clientIdDao().insertClientId(client)
    }
}

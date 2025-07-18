package com.androbeat.androbeatagent.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androbeat.androbeatagent.data.model.models.Client

@Dao
interface ClientIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClientId(clientId: Client)

    @Query("SELECT * FROM client_id WHERE id == 1 LIMIT 1")
    suspend fun getClientId(): Client?
}
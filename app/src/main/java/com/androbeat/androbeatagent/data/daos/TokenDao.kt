package com.androbeat.androbeatagent.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androbeat.androbeatagent.data.model.models.TokenModel

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: TokenModel)

    @Query("SELECT * FROM token_table LIMIT 1")
    suspend fun getToken(): TokenModel?
}
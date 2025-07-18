package com.androbeat.androbeatagent.data.model.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class TokenModel(
    @PrimaryKey val id: Int = 0,
    val token: String
)
package com.androbeat.androbeatagent.data.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_id")
data class Client(
    @PrimaryKey val id: Int,
    val name: String
)
package com.androbeat.androbeatagent.data.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deviceId")
data class DeviceId(
    @PrimaryKey val id: Int,
    val deviceId: String
)
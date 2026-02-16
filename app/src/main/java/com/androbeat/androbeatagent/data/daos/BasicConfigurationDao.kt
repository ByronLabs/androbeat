package com.androbeat.androbeatagent.data.daos


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androbeat.androbeatagent.data.model.models.DeviceId

@Dao
interface DeviceIdDao {
    @Insert
    suspend fun insertDeviceId(deviceId: DeviceId)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDeviceId(deviceId: DeviceId)

    @Query("SELECT * FROM deviceId")
    suspend fun getDeviceId(): DeviceId?

    @Update
    suspend fun updateDeviceId(deviceId: DeviceId)

    @Query("DELETE FROM deviceId")
    suspend fun clear()
}

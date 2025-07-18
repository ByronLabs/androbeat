package com.androbeat.androbeatagent.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel

@Dao
interface ElasticDataDao {
    @get:Query("SELECT * FROM elasticdatamodel")
    val all: List<ElasticDataModel?>?

    @Insert
    fun insert(vararg elasticDataModels: ElasticDataModel)

    @Delete
    fun delete(vararg elasticDataModels: ElasticDataModel)
}
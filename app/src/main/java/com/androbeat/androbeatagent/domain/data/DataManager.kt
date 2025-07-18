package com.androbeat.androbeatagent.domain.data

import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel

interface DataManager {
    fun saveDataOnElasticSearch(data: ElasticDataModel, isFromCache: Boolean)
    fun saveDataOnElasticSearch()
}
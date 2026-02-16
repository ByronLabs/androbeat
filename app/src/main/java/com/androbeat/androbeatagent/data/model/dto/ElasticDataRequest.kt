package com.androbeat.androbeatagent.data.model.dto

import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel

data class ElasticDataRequest(
    val index: String,
    val document: ElasticDataModel
)

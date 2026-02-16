package com.androbeat.androbeatagent.data.remote.rest.logstash

import com.androbeat.androbeatagent.data.model.dto.ElasticDataRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LogstashApiInterface {
    @POST("/")
    suspend fun sendToLogstash(@Body data: ElasticDataRequest): Response<Unit>
}

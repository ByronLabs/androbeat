package com.androbeat.androbeatagent.domain.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.model.models.elastic.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ElasticApiInterface {

    @POST("/{target}/_doc")
    fun saveData(
        @Path(value = "target", encoded = true) target: String?,
        @Body data: ElasticDataModel?
    ): Call<ResponseData?>?

    @PUT("/{target}")
    fun createIndex(
        @Path(value = "target", encoded = true) target: String?
    ): Call<ResponseData?>?

    @GET("/{target}")
    fun checkIndexExists(
        @Path(value = "target", encoded = true) target: String?
    ): Call<ResponseData?>?
}
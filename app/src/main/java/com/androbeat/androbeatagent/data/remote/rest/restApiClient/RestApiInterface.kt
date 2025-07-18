package com.androbeat.androbeatagent.data.remote.rest.restApiClient


import com.androbeat.androbeatagent.data.model.models.communication.Device
import com.androbeat.androbeatagent.data.model.models.communication.ReinstallAgentRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RestApiInterface {

    @POST("users/me")
    fun getUser(): Call<String>

    @POST("registerDevice")
    fun registerDevice(@Body device: Device): Call<String>

    @POST("modifyDeviceId")
    fun modifyDeviceId(oldDeviceId: String, newDeviceId:String): Call<String>

    @POST("resetDevice")
    fun reinstallAgent(@Body request: ReinstallAgentRequest): Call<String>

    @GET("checkTokenStatus")
    fun checkTokenStatus(@Query("enrollToken") token: String): Call<String>

}
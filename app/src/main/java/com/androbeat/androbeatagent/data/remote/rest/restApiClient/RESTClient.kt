package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import android.content.Context
import com.androbeat.androbeatagent.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RESTClient {

    private const val BASE_URL = "https://10.0.2.2:8000/"

    fun getRetrofit(context: Context): Retrofit {

        val client = SelfSignedSslCertsUtils.getSelfSignedSslClient(context, R.raw.cert)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import android.content.Context
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RESTClient {

    fun getRetrofit(context: Context): Retrofit {

        val tokenManager = SecureTokenManager(context)
        val client = SelfSignedSslCertsUtils.getSelfSignedSslClient(context, R.raw.cert)
            .newBuilder()
            .addInterceptor(JwtInterceptor(tokenManager, SessionManager(tokenManager)))
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

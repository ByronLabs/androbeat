package com.androbeat.androbeatagent.data.remote.rest.elastic

import com.androbeat.androbeatagent.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Credentials.basic
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ElasticConnector {
    private var retrofit: Retrofit? = null

    private fun createOkHttpClient(): OkHttpClient {
        val basicAuth = basic(BuildConfig.ELASTIC_USER, BuildConfig.ELASTIC_PASS)
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG_RETROFIT) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            clientBuilder.addInterceptor(interceptor)
        }
        return clientBuilder.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .header("Authorization", basicAuth)
                .build()
            chain.proceed(newRequest)
        }.build()
    }

    private fun createGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()
        return GsonConverterFactory.create(gson)
    }

    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    baseUrl(BuildConfig.ELASTIC_HOST)
                    addConverterFactory(createGsonConverterFactory())
                    client(createOkHttpClient())
                }.build()
            }
            return retrofit
        }
}
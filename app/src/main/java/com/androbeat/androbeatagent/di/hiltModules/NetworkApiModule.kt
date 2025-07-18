package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.SelfSignedSslCertsUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    private const val BASE_URL = "https://10.0.2.2:8000/"

    @Singleton
    @Provides
    fun provideRestApiInterface(context: Context): RestApiInterface {
        val client = SelfSignedSslCertsUtils.getSelfSignedSslClient(context, R.raw.cert)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApiInterface::class.java)
    }
}
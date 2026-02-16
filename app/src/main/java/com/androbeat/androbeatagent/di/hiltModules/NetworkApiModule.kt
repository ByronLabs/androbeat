package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.SelfSignedSslCertsUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    @Singleton
    @Provides
    fun provideLogstashApiInterface(@ApplicationContext context: Context): LogstashApiInterface {
        val clientBuilder = SelfSignedSslCertsUtils
            .getSelfSignedSslClient(context, R.raw.cert)
            .newBuilder()
        if (BuildConfig.DEBUG_RETROFIT) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            clientBuilder.addInterceptor(interceptor)
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.LOGSTASH_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LogstashApiInterface::class.java)
    }
}

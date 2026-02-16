package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.ITokenManager
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.JwtInterceptor
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.SecureTokenManager
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.SessionManager
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.SelfSignedSslCertsUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): ITokenManager =
        SecureTokenManager(context)

    @Singleton
    @Provides
    fun provideSessionManager(tokenManager: ITokenManager): SessionManager =
        SessionManager(tokenManager)

    @Singleton
    @Provides
    fun provideJwtInterceptor(
        tokenManager: ITokenManager,
        sessionManager: SessionManager
    ): JwtInterceptor = JwtInterceptor(tokenManager, sessionManager)

    @Singleton
    @Provides
    fun provideRestApiInterface(
        @ApplicationContext context: Context,
        jwtInterceptor: JwtInterceptor
    ): RestApiInterface {
        val client = SelfSignedSslCertsUtils.getSelfSignedSslClient(context, R.raw.cert)
            .newBuilder()
            .addInterceptor(jwtInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApiInterface::class.java)
    }

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

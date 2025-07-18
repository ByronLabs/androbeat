package com.androbeat.androbeatagent.di.hiltModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomExecutorProvider {
    @Provides
    @Singleton
    @Named("RoomExecutor")
    fun provideRoomExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }


    @Provides
    @Singleton
    @Named("ReconnectExecutor")
    fun provideReconnectExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}
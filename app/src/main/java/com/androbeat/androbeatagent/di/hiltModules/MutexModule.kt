// src/main/java/com/androbeat/androbeatagent/di/hiltModules/MutexModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.sync.Mutex
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MutexModule {

    @Provides
    @Singleton
    fun provideMutex(): Mutex {
        return Mutex()
    }
}
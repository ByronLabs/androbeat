// src/main/java/com/androbeat/androbeatagent/di/hiltModules/LogReaderModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.local.logReader.LogReaderImp
import com.androbeat.androbeatagent.domain.data.LogReader

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LogReaderModule {

    @Provides
    @Singleton
    fun provideLogReader(): LogReader {
        return LogReaderImp()
    }
}
// src/main/java/com/androbeat/androbeatagent/di/hiltModules/ExtractorFactoryModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.local.extractors.ExtractorFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExtractorFactoryModule {

    @Provides
    @Singleton
    fun provideExtractorFactory(): ExtractorFactory {
        return ExtractorFactory
    }
}
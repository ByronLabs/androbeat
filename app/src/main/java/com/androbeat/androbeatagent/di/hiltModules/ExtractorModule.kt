package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExtractorModule {

    @Provides
    @Singleton
    fun provideExtractors(): List<DataExtractor> {
        return mutableListOf()
    }
}
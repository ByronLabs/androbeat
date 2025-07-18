package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.domain.data.DataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProvidersModule {

    @Provides
    fun provideProviders(): MutableList<DataProvider<*>> {
        return mutableListOf()
    }
}

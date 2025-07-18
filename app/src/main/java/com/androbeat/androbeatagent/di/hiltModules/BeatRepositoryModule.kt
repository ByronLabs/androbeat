package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.data.repository.BeatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BeatRepositoryModule {

    @Provides
    @Singleton
    fun provideBeatRepository(beatDataSource: BeatDataSource): BeatRepository {
        return BeatRepository(beatDataSource)
    }
}
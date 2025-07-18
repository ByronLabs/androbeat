package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.local.BeatService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BeatServiceModule {

    @Provides
    fun provideBeatService(): BeatService {
        return BeatService()
    }
}
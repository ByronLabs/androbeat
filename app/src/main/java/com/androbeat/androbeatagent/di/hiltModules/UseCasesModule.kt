package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager
import com.androbeat.androbeatagent.data.service.BeatServiceManager
import com.androbeat.androbeatagent.domain.usecase.StartSensorUseCase
import com.androbeat.androbeatagent.domain.usecase.StopSensorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideBeatServiceManager(context: Context): IBeatServiceManager {
        return BeatServiceManager(context)
    }

    @Provides
    @Singleton
    fun provideStartSensorUseCase(beatServiceManager: IBeatServiceManager): StartSensorUseCase {
        return StartSensorUseCase(beatServiceManager)
    }

    @Provides
    @Singleton
    fun provideStopSensorUseCase(beatServiceManager: IBeatServiceManager): StopSensorUseCase {
        return StopSensorUseCase(beatServiceManager)
    }
}
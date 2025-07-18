// src/main/java/com/androbeat/androbeatagent/di/hiltModules/SensorFactoryModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.local.sensors.SensorFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorFactoryModule {

    @Provides
    @Singleton
    fun provideSensorFactory(): SensorFactory {
        return SensorFactory
    }
}
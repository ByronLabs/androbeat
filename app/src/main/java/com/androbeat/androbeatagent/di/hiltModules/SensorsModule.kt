// src/main/java/com/androbeat/androbeatagent/di/hiltModules/SensorModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.local.sensors.AndroidSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideSensors(): List<AndroidSensor> {
        return mutableListOf()
    }
}
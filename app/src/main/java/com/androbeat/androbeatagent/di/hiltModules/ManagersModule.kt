package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.repository.INetworkHandler
import com.androbeat.androbeatagent.data.repository.NetworkHandler
import com.androbeat.androbeatagent.data.repository.managers.ExtractorManager
import com.androbeat.androbeatagent.data.repository.managers.SensorManager
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import com.androbeat.androbeatagent.domain.data.IExtractorManager
import com.androbeat.androbeatagent.domain.data.ISensorManager
import com.androbeat.androbeatagent.domain.data.IServiceManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagersModule {

    @Binds
    abstract fun bindServiceManager(serviceManager: ServiceManager): IServiceManager

    @Binds
    abstract fun bindSensorManager(sensorManager: SensorManager): ISensorManager

    @Binds
    abstract fun bindExtractorManager(extractorManager: ExtractorManager): IExtractorManager

    @Binds
    abstract fun bindNetworkHandler(networkHandler: NetworkHandler): INetworkHandler
}
// AppModule.kt
package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.data.local.extractors.ExtractorFactory
import com.androbeat.androbeatagent.data.local.sensors.SensorFactory
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.data.repository.NetworkHandler
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import com.androbeat.androbeatagent.data.repository.managers.DataProcessingManager
import com.androbeat.androbeatagent.data.repository.managers.ExtractorManager
import com.androbeat.androbeatagent.data.repository.managers.SensorManager
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import com.androbeat.androbeatagent.domain.data.LogReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeatDataSource(
        serviceManager: ServiceManager,
        dataProcessingManager: DataProcessingManager,
        context: Context,
        networkHandler: NetworkHandler,
        @Named("ReconnectExecutor") reconnectExecutor: ExecutorService
    ): BeatDataSource {
        return BeatDataSource(
            serviceManager = serviceManager,
            dataProcessingManager = dataProcessingManager,
            context = context,
            networkHandler = networkHandler,
            reconnectExecutor = reconnectExecutor
        )
    }

    @Provides
    @Singleton
    fun provideNetworkHandler(
        appDatabase: AppDatabase,
        dataManager: DataManagerImp
    ): NetworkHandler {
        return NetworkHandler(appDatabase, dataManager)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSensorManager(
        @ApplicationContext context: Context,
        logger: Logger
    ): SensorManager {
        return SensorManager(context, SensorFactory)
    }

    @Provides
    @Singleton
    fun provideExtractorManager(@ApplicationContext context: Context): ExtractorManager {
        return ExtractorManager(context, ExtractorFactory)
    }

}
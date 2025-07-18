package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import com.androbeat.androbeatagent.domain.data.DataManager
import com.androbeat.androbeatagent.domain.elastic.ElasticApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Changed to SingletonComponent for application-wide provision
object DataModule {
    @Provides
    fun dataModuleProvider(@ApplicationContext context: Context, @Named("RoomExecutor") roomExecutor: ExecutorService,
                           apiInterface: ElasticApiInterface,
                           appDatabase: AppDatabase,
                           dateFormatter: SimpleDateFormat
    ): DataManager {
        return DataManagerImp(context,roomExecutor, apiInterface, appDatabase,dateFormatter)
    }

    @Provides
    @Singleton
    @Named("DateFormatter")
    fun provideDateFormatter(): SimpleDateFormat {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    }
}
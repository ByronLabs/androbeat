package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import androidx.room.Room
import com.androbeat.androbeatagent.data.repository.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "androbeat-database"
        ).fallbackToDestructiveMigration().build()
    }
}
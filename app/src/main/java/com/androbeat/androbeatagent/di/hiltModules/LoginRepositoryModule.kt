package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.LoginDataSource
import com.androbeat.androbeatagent.data.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(database: AppDatabase): LoginRepository {
        return LoginRepository(LoginDataSource(database))
    }
}

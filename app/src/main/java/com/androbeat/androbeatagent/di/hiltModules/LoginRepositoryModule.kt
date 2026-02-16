package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.ITokenManager
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
    fun provideLoginRepository(
        apiService: RestApiInterface,
        database: AppDatabase,
        tokenManager: ITokenManager
    ): LoginRepository {
        return LoginRepository(LoginDataSource(apiService, database, tokenManager))
    }
}

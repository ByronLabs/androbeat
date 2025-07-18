package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.data.remote.rest.elastic.ElasticConnector
import com.androbeat.androbeatagent.domain.elastic.ElasticApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ElasticModule {
    @Provides
    fun provideElasticConnector(): ElasticConnector {
        return ElasticConnector
    }

    @Provides
    fun provideElasticApiInterface(elasticConnector: ElasticConnector): ElasticApiInterface {
        return elasticConnector.client?.create(ElasticApiInterface::class.java)!!
    }
}
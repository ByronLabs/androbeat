package com.androbeat.androbeatagent.hiltTestModules


import com.androbeat.androbeatagent.di.hiltModules.PermissionsModule
import com.androbeat.androbeatagent.domain.permissions.PermissionsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PermissionsModule::class]
)
object PermissionsManagerTestModule {
    @Provides
    fun providePermissionsManager(): PermissionsManager = mockk(relaxed = true)
}
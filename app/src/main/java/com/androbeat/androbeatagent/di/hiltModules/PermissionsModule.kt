package com.androbeat.androbeatagent.di.hiltModules


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.permissions.PermissionsManager
import com.androbeat.androbeatagent.data.permissions.PermissionsCheckerImpl
import com.androbeat.androbeatagent.data.permissions.PermissionsManagerImp
import com.androbeat.androbeatagent.data.permissions.PermissionsRequesterImpl
import com.androbeat.androbeatagent.domain.permissions.PermissionsChecker
import com.androbeat.androbeatagent.domain.permissions.PermissionsRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
@Module
@InstallIn(ActivityComponent::class)
object PermissionsModule {

    @Provides
    fun providePermissionsChecker(@ActivityContext context: Context): PermissionsChecker {
        return PermissionsCheckerImpl(context as AppCompatActivity)
    }

    @Provides
    fun providePermissionsRequester(@ActivityContext context: Context): PermissionsRequester {
        return PermissionsRequesterImpl(context as AppCompatActivity)
    }

    @Provides
    fun providePermissionsManager(
        @ActivityContext context: Context,
        permissionsChecker: PermissionsChecker,
        permissionsRequester: PermissionsRequester,
        logger: Logger // Añade el parámetro logger
    ): PermissionsManager {
        return PermissionsManagerImp(context as AppCompatActivity, permissionsChecker, permissionsRequester)
    }
}
package com.androbeat.androbeatagent.di.hiltModules

import com.androbeat.androbeatagent.domain.communication.NotificationHelper
import com.androbeat.androbeatagent.domain.communication.UrgentNotificationHelper
import com.androbeat.androbeatagent.presentation.notifications.NotificationHelperImp
import com.androbeat.androbeatagent.presentation.notifications.UrgentNotificationHelperImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    fun provideNotifiacionHelper(): NotificationHelper {
        return NotificationHelperImp
    }

    @Provides
    fun provideUrgentNotifiacionHelper(): UrgentNotificationHelperImp.Companion {
        return UrgentNotificationHelperImp
    }
}


package com.androbeat.androbeatagent.di.hiltModules

import android.content.Context
import com.androbeat.androbeatagent.data.actions.ActionDispatcher
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.permissions.DeviceOwnerManager
import com.androbeat.androbeatagent.presentation.notifications.NotificationHelperImp
import com.androbeat.androbeatagent.presentation.notifications.UrgentNotificationHelperImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object CommunicationModule {

    @Provides
    fun provideNotificationHelperImp(context: Context): NotificationHelperImp {
        return NotificationHelperImp
    }

    @Provides
    fun provideActionDispatcher(deviceOwnerManager: DeviceOwnerManager): ActionDispatcher {
        return ActionDispatcher(deviceOwnerManager)
    }

    @Provides
    fun provideDeviceOwnerManager(context: Context, urgentNotificationHelperImp: UrgentNotificationHelperImp): DeviceOwnerManager {
        return DeviceOwnerManager(context, urgentNotificationHelperImp)
    }
}
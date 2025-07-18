package com.androbeat.androbeatagent.utils.helpers

import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
import com.androbeat.androbeatagent.domain.communication.UrgentNotificationHelper
import com.androbeat.androbeatagent.presentation.notifications.UrgentNotificationHelperImp
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UrgentNotificationHelperImpTest {

    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var urgentNotificationHelper: UrgentNotificationHelper

    @Before
    fun setUp() {

        context = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)

        every { context.getSystemService(Context.NOTIFICATION_SERVICE) } returns notificationManager

        mockkConstructor(AudioAttributes.Builder::class)

        val audioAttributesBuilderMock = mockk<AudioAttributes.Builder>(relaxed = true)
        val audioAttributesMock = mockk<AudioAttributes>(relaxed = true)

        every { anyConstructed<AudioAttributes.Builder>().setUsage(any()) } returns audioAttributesBuilderMock
        every { audioAttributesBuilderMock.setContentType(any()) } returns audioAttributesBuilderMock
        every { audioAttributesBuilderMock.build() } returns audioAttributesMock

        urgentNotificationHelper = UrgentNotificationHelperImp.getInstance(context)
    }

    @Test
    fun `sendEmergencyNotification should call notify on NotificationManager`() {
        mockkConstructor(NotificationCompat.Builder::class)

        val builderMock = mockk<NotificationCompat.Builder>(relaxed = true)

        every { anyConstructed<NotificationCompat.Builder>().setSmallIcon(any<Int>()) } returns builderMock
        every { anyConstructed<NotificationCompat.Builder>().setContentTitle(any()) } returns builderMock
        every { anyConstructed<NotificationCompat.Builder>().setContentText(any()) } returns builderMock
        every { anyConstructed<NotificationCompat.Builder>().setPriority(any()) } returns builderMock
        every { anyConstructed<NotificationCompat.Builder>().build() } returns mockk()

        urgentNotificationHelper.sendEmergencyNotification(
            title = "Emergency Alert",
            message = "This is an emergency notification."
        )

        verify { notificationManager.notify(any(), any()) }
    }
}

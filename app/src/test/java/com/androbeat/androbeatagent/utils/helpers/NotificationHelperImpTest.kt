package com.androbeat.androbeatagent.utils.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.androbeat.androbeatagent.presentation.view.MainActivity
import com.androbeat.androbeatagent.presentation.notifications.NotificationHelperImp
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class NotificationHelperImpTest {

    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)
        notificationChannel = mockk(relaxed = true)

        every { context.getSystemService(Context.NOTIFICATION_SERVICE) } returns notificationManager
        mockkStatic(Build.VERSION::class)
        mockkObject(NotificationHelperImp)
    }

    @After
    fun tearDown() {
        unmockkStatic(Build.VERSION::class)
        unmockkObject(NotificationHelperImp)
    }

    @Test
    fun createNotificationChannelShouldCreateChannelOnOreoAndAbove() {
        every { NotificationHelperImp.isOreoOrAbove() } returns true

        NotificationHelperImp.createNotificationChannel(context)

        verify { notificationManager.createNotificationChannel(any()) }
    }

    @Test
    fun createNotificationChannelShouldNotCreateChannelBelowOreo() {
        every { NotificationHelperImp.isOreoOrAbove() } returns false

        NotificationHelperImp.createNotificationChannel(context)

        verify(exactly = 0) { notificationManager.createNotificationChannel(any()) }
    }

    @Test
    fun createNotificationShouldBuildNotification() {
        val title = "Test Title"
        val message = "Test Message"
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = mockk(relaxed = true)
        val notification: Notification = mockk(relaxed = true)

        mockkStatic(PendingIntent::class)
        every { PendingIntent.getActivity(context, 0, intent, any()) } returns pendingIntent
        mockkConstructor(NotificationCompat.Builder::class)
        every { anyConstructed<NotificationCompat.Builder>().build() } returns notification

        mockkStatic(NotificationManagerCompat::class)
        every { NotificationManagerCompat.from(context).notify(1, notification) } just Runs

        NotificationHelperImp.createNotification(context, title, message)

        verify { NotificationManagerCompat.from(context).notify(1, notification) }
    }

    @Test
    fun createServiceNotificationShouldReturnNotification() {
        val notification: Notification = mockk(relaxed = true)
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = mockk(relaxed = true)

        mockkStatic(PendingIntent::class)
        every { PendingIntent.getActivity(context, 0, notificationIntent, any()) } returns pendingIntent
        mockkConstructor(NotificationCompat.Builder::class)
        every { anyConstructed<NotificationCompat.Builder>().build() } returns notification

        val result = NotificationHelperImp.createServiceNotification(context)

        assert(result == notification)
    }

}
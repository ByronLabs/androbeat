package com.androbeat.androbeatagent.data.repository.managers

import android.app.Notification
import android.content.Context
import android.os.PowerManager
import com.androbeat.androbeatagent.domain.communication.NotificationHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BeatBeatServiceManagerTest {

    private lateinit var context: Context
    private lateinit var powerManager: PowerManager
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var serviceManager: ServiceManager

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        powerManager = mockk(relaxed = true)
        notificationHelper = mockk(relaxed = true)
        serviceManager = ServiceManager(context, powerManager, notificationHelper)
    }


    @Test
    fun testCreateServiceNotification() {
        val notification = mockk<Notification>()
        every { notificationHelper.createServiceNotification(context) } returns notification

        val result = serviceManager.createServiceNotification()

        assertNotNull(result)
        verify { notificationHelper.createServiceNotification(context) }
    }

}
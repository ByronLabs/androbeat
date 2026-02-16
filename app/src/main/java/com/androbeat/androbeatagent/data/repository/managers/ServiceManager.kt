package com.androbeat.androbeatagent.data.repository.managers

import android.app.Notification
import android.app.Service
import android.content.Context
import android.os.PowerManager
import androidx.work.WorkManager
import com.androbeat.androbeatagent.data.work.ElasticWorkScheduler
import com.androbeat.androbeatagent.domain.communication.NotificationHelper
import com.androbeat.androbeatagent.domain.data.IServiceManager
import com.androbeat.androbeatagent.utils.ServiceUtils
import javax.inject.Inject

class ServiceManager @Inject constructor(
    private val context: Context,
    private val powerManager: PowerManager,
    private val notificationHelper: NotificationHelper
) : IServiceManager {
    private lateinit var wakeLock: PowerManager.WakeLock
    private var _service: Service? = null

    override fun setupService() {
        setupNotification()
        acquireWakeLock()
        ElasticWorkScheduler.schedulePeriodicFlush(WorkManager.getInstance(context))
    }

    private fun setupNotification() {
        notificationHelper.createNotificationChannel(context)
    }

    fun createServiceNotification(): Notification {
        return notificationHelper.createServiceNotification(context)
    }

    private fun acquireWakeLock() {
        wakeLock = ServiceUtils.setWakeLock(powerManager)
        wakeLock.acquire(600000)
    }

    fun releaseWakeLock() {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    fun setService(service: Service) {
        _service = service
    }

    fun onDestroy() {
        releaseWakeLock()
    }
}

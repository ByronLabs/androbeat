package com.androbeat.androbeatagent.presentation.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.domain.communication.UrgentNotificationHelper
import java.lang.ref.WeakReference
import javax.inject.Inject


class UrgentNotificationHelperImp   @Inject constructor(context: Context) : UrgentNotificationHelper {

    private val contextRef: WeakReference<Context> = WeakReference(context)

    companion object {
        private var instance: WeakReference<UrgentNotificationHelperImp>? = null
        fun getInstance(context: Context): UrgentNotificationHelperImp {
            return instance?.get() ?: synchronized(this) {
                instance?.get() ?: UrgentNotificationHelperImp(context).also {
                    instance = WeakReference(it)
                }
            }
        }
        private const val EMERGENCY_CHANNEL_ID = "emergency_channel"
        private const val EMERGENCY_CHANNEL_NAME = "Notificaciones de Emergencia"
    }

    init {
        createEmergencyNotificationChannel()
    }

    private fun createEmergencyNotificationChannel() {
        val context = contextRef.get() ?: return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            EMERGENCY_CHANNEL_ID,
            EMERGENCY_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para notificaciones urgentes de emergencia"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

        notificationManager.createNotificationChannel(channel)
    }

    override fun sendEmergencyNotification(title: String, message: String) {
        val context = contextRef.get() ?: return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, EMERGENCY_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVibrate(longArrayOf(0, 500, 1000, 500, 1000))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
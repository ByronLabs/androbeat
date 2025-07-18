package com.androbeat.androbeatagent.domain.communication

import android.app.Notification
import android.content.Context

interface NotificationHelper {
    fun createNotificationChannel(context: Context)
    fun createNotification(context: Context, title: String, message: String)
    fun createServiceNotification(context: Context) : Notification
}
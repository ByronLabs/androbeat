package com.androbeat.androbeatagent.domain.communication

interface UrgentNotificationHelper {
    fun sendEmergencyNotification(title: String, message: String)
}
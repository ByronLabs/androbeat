package com.androbeat.androbeatagent.domain.service

import android.content.Intent

interface IBeatServiceManager {
    fun startService(serviceIntent: Intent)
    fun stopService(serviceIntent: Intent)
}
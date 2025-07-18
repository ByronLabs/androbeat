package com.androbeat.androbeatagent.data.service

import android.content.Context
import android.content.Intent
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager


class BeatServiceManager(private val context: Context) :
    IBeatServiceManager {
    override fun startService(serviceIntent: Intent) {
        context.startForegroundService(serviceIntent)
    }

    override fun stopService(serviceIntent: Intent) {
        context.stopService(serviceIntent)
    }
}
// BeatService.kt
package com.androbeat.androbeatagent.data.local

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeatService : Service() {

    @Inject
    lateinit var beatDataSource: BeatDataSource

    @Inject
    lateinit var serviceManager: ServiceManager

     override fun onCreate() {
        super.onCreate()
        serviceManager.setService(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("BeatService", "onStartCommand invoked")
        serviceManager.setupService()
        startForegroundService()
        beatDataSource.startBeat(intent)
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = serviceManager.createServiceNotification()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        beatDataSource.destroy()
        serviceManager.releaseWakeLock()
        stopSelf()
        super.onDestroy()
    }
}
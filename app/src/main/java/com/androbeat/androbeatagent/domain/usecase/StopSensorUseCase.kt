package com.androbeat.androbeatagent.domain.usecase

import android.content.Intent
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager

class StopSensorUseCase(private val serviceManager: IBeatServiceManager) {
    fun stopBeatService(serviceIntent: Intent) {
        serviceManager.stopService(serviceIntent)
    }
}
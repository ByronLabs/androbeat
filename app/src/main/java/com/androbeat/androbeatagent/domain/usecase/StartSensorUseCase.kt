package com.androbeat.androbeatagent.domain.usecase

import android.content.Intent
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager

class StartSensorUseCase(private val serviceManager: IBeatServiceManager) {

    fun startBeatService(serviceIntent: Intent) {
        serviceManager.startService(serviceIntent)
    }

}
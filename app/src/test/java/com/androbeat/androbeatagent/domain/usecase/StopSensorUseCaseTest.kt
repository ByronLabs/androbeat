package com.androbeat.androbeatagent.domain.usecase

import android.content.Intent
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager
import com.androbeat.androbeatagent.domain.usecase.StopSensorUseCase
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class StopSensorUseCaseTest {

    private lateinit var serviceManager: IBeatServiceManager
    private lateinit var stopSensorUseCase: StopSensorUseCase

    @Before
    fun setUp() {
        serviceManager = mockk(relaxed = true)
        stopSensorUseCase = StopSensorUseCase(serviceManager)
    }

    @Test
    fun testStopBeatService() {
        val serviceIntent = Intent()
        stopSensorUseCase.stopBeatService(serviceIntent)
        verify { serviceManager.stopService(serviceIntent) }
    }
}
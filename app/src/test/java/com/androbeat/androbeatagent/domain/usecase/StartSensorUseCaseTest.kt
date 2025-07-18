package com.androbeat.androbeatagent.domain.usecase

import android.content.Intent
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager
import com.androbeat.androbeatagent.domain.usecase.StartSensorUseCase
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class StartSensorUseCaseTest {

   private lateinit var serviceManager: IBeatServiceManager
   private lateinit var startSensorUseCase: StartSensorUseCase

   @Before
   fun setUp() {
     serviceManager = mockk(relaxed = true)
     startSensorUseCase = StartSensorUseCase(serviceManager)
   }

   @Test
   fun testStartBeatService() {
     val serviceIntent = Intent()
     startSensorUseCase.startBeatService(serviceIntent)
     verify { serviceManager.startService(serviceIntent) }
   }
}
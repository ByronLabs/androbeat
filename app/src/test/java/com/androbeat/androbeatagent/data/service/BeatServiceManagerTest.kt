package com.androbeat.androbeatagent.data.service

import android.content.Context
import android.content.Intent
import com.androbeat.androbeatagent.data.service.BeatServiceManager
import com.androbeat.androbeatagent.domain.service.IBeatServiceManager

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class BeatServiceManagerTest {

    private lateinit var context: Context
    private lateinit var beatServiceManager: IBeatServiceManager

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        beatServiceManager = BeatServiceManager(context)
    }

    @Test
    fun testStartService() {
        val serviceIntent = Intent()
        beatServiceManager.startService(serviceIntent)
        verify { context.startForegroundService(serviceIntent) }
    }

    @Test
    fun testStopService() {
        val serviceIntent = Intent()
        beatServiceManager.stopService(serviceIntent)
        verify { context.stopService(serviceIntent) }
    }
}
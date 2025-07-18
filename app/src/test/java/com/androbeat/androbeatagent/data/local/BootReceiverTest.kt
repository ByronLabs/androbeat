package com.androbeat.androbeatagent.data.local

import android.content.Context
import android.content.Intent
import com.androbeat.androbeatagent.data.logger.Logger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class BootReceiverTest {
    private lateinit var bootReceiver: BootReceiver
    private lateinit var context: Context
    private lateinit var intent: Intent
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        bootReceiver = BootReceiver()
        logger = mockk(relaxed = true)
        context = mockk(relaxed = true)
        intent = mockk(relaxed = true)
    }

    @Test
    fun testOnReceive() {
        every { intent.action } returns Intent.ACTION_BOOT_COMPLETED
        bootReceiver.onReceive(context, intent)
        verify { context.startService(any()) }
    }
}
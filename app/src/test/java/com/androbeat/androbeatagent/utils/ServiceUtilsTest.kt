// ServiceUtilsTest.kt
package com.androbeat.androbeatagent.utils

import android.os.PowerManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServiceUtilsTest {

    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock

    @Before
    fun setUp() {
        powerManager = mockk(relaxed = true)
        wakeLock = mockk(relaxed = true)
    }

    @Test
    fun setWakeLock_shouldAcquireWakeLock() {
        every { powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Androbeat::BeatService-WakeLock") } returns wakeLock
        every { wakeLock.isHeld } returns true
        val result = ServiceUtils.setWakeLock(powerManager)
        verify { wakeLock.acquire(10 * 60 * 1000L) }
        assertTrue(result.isHeld)
    }

    @Test
    fun releaseWakeLock_shouldReleaseWakeLockIfHeld() {
        every { wakeLock.isHeld } returns true
        ServiceUtils.releaseWakeLock(wakeLock)
        verify { wakeLock.release() }
    }

    @Test
    fun releaseWakeLock_shouldNotReleaseWakeLockIfNotHeld() {
        every { wakeLock.isHeld } returns false
        ServiceUtils.releaseWakeLock(wakeLock)
        verify(exactly = 0) { wakeLock.release() }
    }
}
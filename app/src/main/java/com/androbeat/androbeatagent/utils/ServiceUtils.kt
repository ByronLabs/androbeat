package com.androbeat.androbeatagent.utils

import android.os.PowerManager

object ServiceUtils {

    open fun setWakeLock(powerManager: PowerManager): PowerManager.WakeLock {
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Androbeat::BeatService-WakeLock")
        wakeLock.acquire(10 * 60 * 1000L) // 10 minutes
        return wakeLock
    }

    open fun releaseWakeLock(wakeLock: PowerManager.WakeLock) {
        if (wakeLock != null && wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}
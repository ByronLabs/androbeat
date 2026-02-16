package com.androbeat.androbeatagent.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.androbeat.androbeatagent.data.repository.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val hasEnrollmentToken = AppDatabase.getDatabase(context)
                    .tokenDao()
                    .getToken()
                    ?.token
                    ?.isNotBlank() == true

                if (!hasEnrollmentToken) {
                    Log.w(TAG, "Boot completed but enrollment token is missing, service start skipped")
                    return@launch
                }

                val serviceIntent = Intent(context, BeatService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
                Log.i(TAG, "BeatService started after boot")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start BeatService after boot: ${e.message}", e)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        private val TAG = BootReceiver::class.java.simpleName
    }
}

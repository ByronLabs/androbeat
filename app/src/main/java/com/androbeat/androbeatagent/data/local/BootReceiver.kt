package com.androbeat.androbeatagent.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, BeatService::class.java)
        context.startService(serviceIntent)
    }

    companion object {
        private val TAG = BootReceiver::class.java.simpleName
    }
}
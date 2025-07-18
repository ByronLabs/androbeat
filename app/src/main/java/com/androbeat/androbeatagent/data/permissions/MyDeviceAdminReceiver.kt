package com.androbeat.androbeatagent.data.permissions

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import com.androbeat.androbeatagent.data.logger.Logger

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Logger.logDebug("MyDeviceAdminReceiver", "Device Admin: enabled")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Logger.logDebug("MyDeviceAdminReceiver", "Device Admin: disabled")
    }
}
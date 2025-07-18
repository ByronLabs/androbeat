package com.androbeat.androbeatagent.domain.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface PermissionsManager {
    fun requestAppPermissions(requestCode: Int)
    fun hasUsageAccessPermission(): Boolean
    fun requestDoNotDisturbPermission(context: Context, activity: Activity)
    fun requestDeviceAdminRights(context: Context, deviceAdminLauncher: ActivityResultLauncher<Intent>)
}
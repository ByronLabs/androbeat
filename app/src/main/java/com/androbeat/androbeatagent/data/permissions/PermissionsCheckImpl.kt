package com.androbeat.androbeatagent.data.permissions

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androbeat.androbeatagent.domain.permissions.PermissionsChecker

class PermissionsCheckerImpl(private val activity: AppCompatActivity) : PermissionsChecker {
    override fun hasSelfPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}
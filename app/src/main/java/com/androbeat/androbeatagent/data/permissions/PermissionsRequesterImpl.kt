package com.androbeat.androbeatagent.data.permissions

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.domain.permissions.PermissionsRequester

class PermissionsRequesterImpl(private val activity: AppCompatActivity) : PermissionsRequester {
    override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}
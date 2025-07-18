package com.androbeat.androbeatagent.domain.permissions

interface PermissionsChecker {
    fun hasSelfPermission(permission: String): Boolean
    fun shouldShowRequestPermissionRationale(permission: String): Boolean
}

interface PermissionsRequester {
    fun requestPermissions(permissions: Array<String>, requestCode: Int)
}
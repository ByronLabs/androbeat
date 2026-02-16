package com.androbeat.androbeatagent.data.permissions

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.permissions.PermissionsManager
import com.androbeat.androbeatagent.presentation.tabFragments.StatusFragment.Companion.PERMISSIONS_REQUEST_CODE
import com.androbeat.androbeatagent.domain.permissions.PermissionsChecker
import com.androbeat.androbeatagent.domain.permissions.PermissionsRequester
import javax.inject.Inject

class PermissionsManagerImp @Inject constructor(
    private val context: Context,
    private val permissionsChecker: PermissionsChecker,
    private val permissionsRequester: PermissionsRequester,
) : PermissionsManager {

    companion object {
        const val LOG_TAG = "PermissionManager"
        @Volatile
        private var dndPermissionRequestedThisSession = false
    }

    override fun requestAppPermissions(requestCode: Int) {
        checkAndRequestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.POST_NOTIFICATIONS
            ), requestCode
        )
        if (!hasUsageAccessPermission()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }
    }

    private fun checkAndRequestPermissions(permissions: Array<String>, requestCode: Int) {
        val permissionsNeeded: MutableList<String> = ArrayList()
        permissions.forEach { permission ->
            if (!permissionsChecker.hasSelfPermission(permission)) {
                permissionsNeeded.add(permission)
            }
        }
        if (permissionsNeeded.isNotEmpty()) {
            permissionsRequester.requestPermissions(permissionsNeeded.toTypedArray(), requestCode)
        }
    }

    override fun hasUsageAccessPermission(): Boolean {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun hasIgnoreBatteryOptimizationsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = context.packageName
            val pm = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            pm.isIgnoringBatteryOptimizations(packageName)
        } else {
            true
        }
    }

    override fun requestDeviceAdminRights(context: Context, deviceAdminLauncher: ActivityResultLauncher<Intent>) {
        val mDPM = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val mAdminName = ComponentName(context, MyDeviceAdminReceiver::class.java)
        if (!mDPM.isAdminActive(mAdminName)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName)
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Add device admin rights to enable device management features."
            )
            deviceAdminLauncher.launch(intent)
        } else {
            Logger.logDebug(LOG_TAG, "Device Admin: already enabled")
        }
    }

    override fun requestDoNotDisturbPermission(context: Context, activity: Activity) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !notificationManager.isNotificationPolicyAccessGranted &&
            !dndPermissionRequestedThisSession
        ) {
            dndPermissionRequestedThisSession = true
            activity.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            Logger.logDebug(LOG_TAG, "Notification policy access settings opened")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSIONS_REQUEST_CODE
            )
        }

        if (notificationManager.isNotificationPolicyAccessGranted) {
            dndPermissionRequestedThisSession = false
        }
    }
}

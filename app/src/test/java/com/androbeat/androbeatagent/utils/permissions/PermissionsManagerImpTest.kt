// PermissionsManagerImpTest.kt
package com.androbeat.androbeatagent.utils.permissions

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.data.permissions.PermissionsManagerImp
import com.androbeat.androbeatagent.domain.permissions.PermissionsChecker
import com.androbeat.androbeatagent.domain.permissions.PermissionsRequester
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class PermissionsManagerImpTest {

    private lateinit var context: Context
    private lateinit var permissionsChecker: PermissionsChecker
    private lateinit var permissionsRequester: PermissionsRequester
    private lateinit var permissionsManager: PermissionsManagerImp
    private lateinit var appOpsManager: AppOpsManager
    private lateinit var packageManager: PackageManager
    private lateinit var applicationInfo: ApplicationInfo
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var activity: Activity
    private lateinit var deviceAdminLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsManagerImp: PermissionsManagerImp


    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        permissionsChecker = mockk(relaxed = true)
        permissionsRequester = mockk(relaxed = true)
        permissionsManager =
            PermissionsManagerImp(context, permissionsChecker, permissionsRequester)


        appOpsManager = mockk()
        packageManager = mockk()
        applicationInfo = mockk()
        devicePolicyManager = mockk()
        notificationManager = mockk()
        activity = mockk()
        deviceAdminLauncher = mockk()
        permissionsManagerImp =
            PermissionsManagerImp(context, permissionsChecker, permissionsRequester)

        every { context.packageManager } returns packageManager
    }

    @Test
    fun hasUsageAccessPermission_shouldReturnTrueWhenPermissionGranted() {
        val appOpsManager = mockk<AppOpsManager>(relaxed = true)
        val packageManager = mockk<PackageManager>(relaxed = true)
        val applicationInfo = mockk<ApplicationInfo>(relaxed = true)

        every { context.packageManager } returns packageManager
        every { packageManager.getApplicationInfo(any(), any<Int>()) } returns applicationInfo
        every { context.getSystemService(Context.APP_OPS_SERVICE) } returns appOpsManager
        every {
            appOpsManager.checkOpNoThrow(
                any(), any(), any()
            )
        } returns AppOpsManager.MODE_ALLOWED

        val result = permissionsManager.hasUsageAccessPermission()

        assertTrue(result)
    }

    @Test
    fun requestAppPermissions_shouldRequestPermissions() {
        val appOpsManager = mockk<AppOpsManager>(relaxed = true)
        val packageManager = mockk<PackageManager>(relaxed = true)
        val applicationInfo = mockk<ApplicationInfo>(relaxed = true)

        every { context.packageManager } returns packageManager
        every { packageManager.getApplicationInfo(any(), any<Int>()) } returns applicationInfo
        every { context.getSystemService(Context.APP_OPS_SERVICE) } returns appOpsManager
        every { permissionsChecker.hasSelfPermission(any()) } returns false
        every { context.startActivity(any()) } just Runs

        permissionsManager.requestAppPermissions(100)

        verify { permissionsRequester.requestPermissions(any(), 100) }
    }

    @Test
    fun checkAndRequestPermissions_shouldRequestPermissionsIfNotGranted() {
        val appOpsManager = mockk<AppOpsManager>(relaxed = true)
        val packageManager = mockk<PackageManager>(relaxed = true)
        val applicationInfo = mockk<ApplicationInfo>(relaxed = true)

        every { context.packageManager } returns packageManager
        every { packageManager.getApplicationInfo(any(), any<Int>()) } returns applicationInfo
        every { context.getSystemService(Context.APP_OPS_SERVICE) } returns appOpsManager
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.POST_NOTIFICATIONS
        )
        every { permissionsChecker.hasSelfPermission(any()) } returns false

        permissionsManager.requestAppPermissions(100)

        verify { permissionsRequester.requestPermissions(permissions, 100) }
    }

    @Test
    fun testRequestDeviceAdminRights() {
        every { context.getSystemService(Context.DEVICE_POLICY_SERVICE) } returns devicePolicyManager
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns false
        every { deviceAdminLauncher.launch(any()) } just Runs

        permissionsManagerImp.requestDeviceAdminRights(context, deviceAdminLauncher)

        verify { deviceAdminLauncher.launch(any()) }
    }


    @Test
    fun testRequestDoNotDisturbPermission() {
        every { context.getSystemService(Context.NOTIFICATION_SERVICE) } returns notificationManager
        every { notificationManager.isNotificationPolicyAccessGranted } returns false
        every {
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
        } returns PackageManager.PERMISSION_DENIED
        every { context.startActivity(any<Intent>()) } just Runs
        permissionsManagerImp.requestDoNotDisturbPermission(context, activity)
        verify { context.startActivity(any<Intent>()) }
    }

}
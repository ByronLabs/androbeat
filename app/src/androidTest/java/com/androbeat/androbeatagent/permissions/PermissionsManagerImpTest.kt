package com.androbeat.androbeatagent.permissions

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.androbeat.androbeatagent.data.permissions.PermissionsManagerImp
import com.androbeat.androbeatagent.domain.permissions.PermissionsChecker
import com.androbeat.androbeatagent.domain.permissions.PermissionsRequester
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PermissionsManagerImpTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private val activity = mockk<AppCompatActivity>()
    private val permissionsChecker = mockk<PermissionsChecker>()
    private val permissionsRequester = mockk<PermissionsRequester>()
    private val packageManager = mockk<PackageManager>()
    private val applicationInfo = mockk<ApplicationInfo>()
    private val appOpsManager = mockk<AppOpsManager>()
    private val powerManager = mockk<android.os.PowerManager>()

    private lateinit var permissionsManagerImp: PermissionsManagerImp

    private val allPossiblePermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @Before
    fun setUp() {
        every { activity.packageManager } returns packageManager
        every { activity.getSystemService(Context.APP_OPS_SERVICE) } returns appOpsManager
        every { activity.getSystemService(Context.POWER_SERVICE) } returns powerManager
        every { activity.packageName } returns "com.androbeat.androbeatagent"
        every { activity.startActivity(any<Intent>()) } just Runs

        every { packageManager.getApplicationInfo(any(), 0) } returns applicationInfo
        every { appOpsManager.checkOpNoThrow(any(), any(), any()) } returns AppOpsManager.MODE_ALLOWED
        every { powerManager.isIgnoringBatteryOptimizations(any()) } returns true

        permissionsManagerImp = PermissionsManagerImp(activity, permissionsChecker, permissionsRequester)
    }

    @Test
    fun requestsPermissionsWhenNoneAreGranted() {
        allPossiblePermissions.forEach { perm ->
            every { permissionsChecker.hasSelfPermission(perm) } returns false
        }

        val permissionsSlot = slot<Array<String>>()
        every { permissionsRequester.requestPermissions(capture(permissionsSlot), 123) } just Runs

        permissionsManagerImp.requestAppPermissions(123)

        verify { permissionsRequester.requestPermissions(any(), 123) }
        assert(permissionsSlot.captured.size == allPossiblePermissions.size)
    }

    @Test
    fun doesNotRequestPermissionsWhenAllAreGranted() {
        allPossiblePermissions.forEach { perm ->
            every { permissionsChecker.hasSelfPermission(perm) } returns true
        }

        permissionsManagerImp.requestAppPermissions(123)

        verify(exactly = 0) { permissionsRequester.requestPermissions(any(), any()) }
    }

    @Test
    fun requestsOnlyUngrantedPermissions() {
        val ungrantedPerms = setOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.BLUETOOTH_CONNECT
        )

        allPossiblePermissions.forEach { perm ->
            every { permissionsChecker.hasSelfPermission(perm) } returns (perm !in ungrantedPerms)
        }

        val permissionsSlot = slot<Array<String>>()
        every { permissionsRequester.requestPermissions(capture(permissionsSlot), 123) } just Runs

        permissionsManagerImp.requestAppPermissions(123)

        verify { permissionsRequester.requestPermissions(any(), 123) }
        assert(permissionsSlot.captured.size == 2)
        assert(ungrantedPerms.all { it in permissionsSlot.captured })
    }

    @Test
    fun startsUsageAccessSettingsWhenUsageAccessIsNotGranted() {
        every { appOpsManager.checkOpNoThrow(any(), any(), any()) } returns AppOpsManager.MODE_ERRORED

        allPossiblePermissions.forEach { perm ->
            every { permissionsChecker.hasSelfPermission(perm) } returns true
        }

        permissionsManagerImp.requestAppPermissions(123)

        verify { activity.startActivity(any<Intent>()) }
    }

    @Test
    fun requestsOnlyNecessaryPermissionsWhenSomeAreGranted() {
        val ungrantedPerms = setOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        allPossiblePermissions.forEach { perm ->
            every { permissionsChecker.hasSelfPermission(perm) } returns (perm !in ungrantedPerms)
        }

        val permissionsSlot = slot<Array<String>>()
        every { permissionsRequester.requestPermissions(capture(permissionsSlot), 123) } just Runs

        permissionsManagerImp.requestAppPermissions(123)

        verify { permissionsRequester.requestPermissions(any(), 123) }
        assert(permissionsSlot.captured.size == 2)
        assert(ungrantedPerms.all { it in permissionsSlot.captured })
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}
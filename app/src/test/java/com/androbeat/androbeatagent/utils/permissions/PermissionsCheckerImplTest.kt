// PermissionsCheckerImplTest.kt
package com.androbeat.androbeatagent.utils.permissions

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androbeat.androbeatagent.data.permissions.PermissionsCheckerImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PermissionsCheckerImplTest {

    private lateinit var activity: AppCompatActivity
    private lateinit var permissionsChecker: PermissionsCheckerImpl

    @Before
    fun setUp() {
        activity = mockk(relaxed = true)
        permissionsChecker = PermissionsCheckerImpl(activity)
    }

    @Test
    fun hasSelfPermission_shouldReturnTrueWhenPermissionGranted() {
        every { ContextCompat.checkSelfPermission(activity, "test.permission") } returns PackageManager.PERMISSION_GRANTED

        val result = permissionsChecker.hasSelfPermission("test.permission")

        assertTrue(result)
    }

    @Test
    fun hasSelfPermission_shouldReturnFalseWhenPermissionDenied() {
        every { ContextCompat.checkSelfPermission(activity, "test.permission") } returns PackageManager.PERMISSION_DENIED

        val result = permissionsChecker.hasSelfPermission("test.permission")

        assertFalse(result)
    }

}
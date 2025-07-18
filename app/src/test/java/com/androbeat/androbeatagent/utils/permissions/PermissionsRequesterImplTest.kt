package com.androbeat.androbeatagent.utils.permissions

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.data.permissions.PermissionsRequesterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test


class PermissionsRequesterImplTest {

    @Test
    fun testRequestPermissions() {
        val activity = mockk<AppCompatActivity>(relaxed = true)
        val permissionsRequester = PermissionsRequesterImpl(activity)
        val permissions = arrayOf("android.permission.CAMERA", "android.permission.READ_CONTACTS")
        val requestCode = 123

        mockkStatic(ActivityCompat::class)
        every { ActivityCompat.requestPermissions(activity, permissions, requestCode) } returns Unit

        permissionsRequester.requestPermissions(permissions, requestCode)

        verify { ActivityCompat.requestPermissions(activity, permissions, requestCode) }
    }
}
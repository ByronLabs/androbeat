package com.androbeat.androbeatagent.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.UserManager
import android.widget.Toast
import com.androbeat.androbeatagent.data.permissions.DeviceOwnerManager
import com.androbeat.androbeatagent.domain.communication.UrgentNotificationHelper
import com.androbeat.androbeatagent.presentation.notifications.UrgentNotificationHelperImp
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeviceOwnerManagerTest {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var deviceOwnerManager: DeviceOwnerManager
    private  var urgentNotificationHelper: UrgentNotificationHelper = mockk<UrgentNotificationHelper>()

    private val devicePolicyManager = mockk<DevicePolicyManager>()
    private val context = mockk<Context>()

    @Before
    fun setUp() {
        connectivityManager = mockk()
        MockKAnnotations.init(this)
        every { context.getSystemService(Context.DEVICE_POLICY_SERVICE) } returns devicePolicyManager
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { context.packageName } returns "com.androbeat.androbeatagent"
        deviceOwnerManager = DeviceOwnerManager(context,urgentNotificationHelper)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testSetPasswordMinimumLength() {
        every { devicePolicyManager.isAdminActive(any()) } returns true
        every { devicePolicyManager.setPasswordMinimumLength(any(), any()) } just Runs

        deviceOwnerManager.setPasswordMinimumLength(8)

        verify { devicePolicyManager.setPasswordMinimumLength(any(), 8) }
    }

    @Test
    fun testLockDevice() {
        val adminComponent = mockk<ComponentName>()
        every { devicePolicyManager.isAdminActive(any()) } returns true
        every { devicePolicyManager.lockNow() } just Runs
        deviceOwnerManager.lockDevice()
        verify { devicePolicyManager.lockNow() }
    }


    @Test
    fun testWipeData() {
        every { devicePolicyManager.isAdminActive(any()) } returns true
        every { devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE) } just Runs

        deviceOwnerManager.wipeData()

        verify { devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE) }
    }

    @Test
    fun testSetDeviceOwner() {
        // Mock the static method Toast.makeText() using mockkStatic
        mockkStatic(Toast::class)

        // Mock behavior of Toast.makeText()
        val toast = mockk<Toast>()
        every { Toast.makeText(any<Context>(), any<CharSequence>(), any()) } returns toast
        every { toast.show() } just Runs

        every { devicePolicyManager.isDeviceOwnerApp(any()) } returns false
        every { devicePolicyManager.setDeviceOwnerLockScreenInfo(any(), any()) } just Runs

        deviceOwnerManager.setDeviceOwner()

        verify { devicePolicyManager.setDeviceOwnerLockScreenInfo(any(), "Device owner lock screen info") }
    }

    @Test
    fun testClearDeviceOwner() {
        every { devicePolicyManager.isDeviceOwnerApp(any()) } returns true
        every { devicePolicyManager.clearDeviceOwnerApp(any()) } just Runs

        deviceOwnerManager.clearDeviceOwner()

        verify { devicePolicyManager.clearDeviceOwnerApp(any()) }
    }

    @Test
    fun testDisableMobileData() {
        // Mock NetworkRequest.Builder
        val networkRequestBuilder = mockk<NetworkRequest.Builder>()
        val networkRequest = mockk<NetworkRequest>()
        every { networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) } returns networkRequestBuilder
        every { networkRequestBuilder.build() } returns networkRequest

        // Mock other dependencies
        every { devicePolicyManager.isAdminActive(any()) } returns true
        every { devicePolicyManager.isDeviceOwnerApp("com.androbeat.androbeatagent") } returns true
        every { devicePolicyManager.addUserRestriction(any(), UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS) } just Runs
        every { connectivityManager.requestNetwork(any(), any<ConnectivityManager.NetworkCallback>()) } just Runs

        // Call the method under test
        deviceOwnerManager.disableMobileData()

        // Verify interactions
        verify { devicePolicyManager.addUserRestriction(any(), UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS) }
    }
}
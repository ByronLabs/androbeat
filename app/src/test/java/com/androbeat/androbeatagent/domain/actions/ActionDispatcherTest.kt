package com.androbeat.androbeatagent.domain.actions

import com.androbeat.androbeatagent.data.remote.communication.ActionMessage
import com.androbeat.androbeatagent.data.actions.ActionDispatcher
import com.androbeat.androbeatagent.data.permissions.DeviceOwnerManager
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.every
import org.junit.Before
import org.junit.Test

class ActionDispatcherTest {

    @MockK
    private lateinit var deviceOwnerManager: DeviceOwnerManager
    private lateinit var actionDispatcher: ActionDispatcher

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        actionDispatcher = ActionDispatcher(deviceOwnerManager)
    }

    @Test
    fun dispatchActionCallsSetDeviceOwnerWhenActionIsSetDeviceOwner() {
        val actionMessage = ActionMessage("SET_DEVICE_OWNER", "Test reason", 0, 0)
        every { deviceOwnerManager.setDeviceOwner() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.setDeviceOwner() }
    }

    @Test
    fun dispatchActionCallsClearDeviceOwnerWhenActionIsClearDeviceOwner() {
        val actionMessage = ActionMessage("CLEAR_DEVICE_OWNER", "Test reason", 0, 0)
        every { deviceOwnerManager.clearDeviceOwner() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.clearDeviceOwner() }
    }

    @Test
    fun dispatchActionCallsLockDeviceWhenActionIsLockDevice() {
        val actionMessage = ActionMessage("LOCK_DEVICE", "Test reason", 0, 0)
        every { deviceOwnerManager.lockDevice() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.lockDevice() }
    }

    @Test
    fun dispatchActionCallsSetPasswordMinimumLengthWhenActionIsSetPasswordMinimumLength() {
        val actionMessage = ActionMessage("SET_PASSWORD_MINIMUM_LENGTH", "Test reason", 0, 0)
        every { deviceOwnerManager.setPasswordMinimumLength(8) } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.setPasswordMinimumLength(8) }
    }

    @Test
    fun dispatchActionCallsWipeDataWhenActionIsWipeData() {
        val actionMessage = ActionMessage("WIPE_DATA", "Test reason", 0, 0)
        every { deviceOwnerManager.wipeData() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.wipeData() }
    }

    @Test
    fun dispatchActionCallsDisableBluetoothWhenActionIsDisableBluetooth() {
        val actionMessage = ActionMessage("DISABLE_BLUETOOTH", "Test reason", 0, 0)
        every { deviceOwnerManager.disableBluetooth() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.disableBluetooth() }
    }

    @Test
    fun dispatchActionCallsSetAirplaneModeWhenActionIsSetAirplaneMode() {
        val actionMessage = ActionMessage("SET_AIRPLANE_MODE", "Test reason", 0, 0)
        every { deviceOwnerManager.setAirplaneMode() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.setAirplaneMode() }
    }

    @Test
    fun dispatchActionCallsDisableMobileDataAndDisableWifiWhenActionIsDisableNetwork() {
        val actionMessage = ActionMessage("DISABLE_NETWORK", "Test reason", 0, 0)
        every { deviceOwnerManager.disableMobileData() } returns Unit
        every { deviceOwnerManager.disableWifi() } returns Unit

        actionDispatcher.dispatchAction(actionMessage)

        verify { deviceOwnerManager.disableMobileData() }
        verify { deviceOwnerManager.disableWifi() }
    }
}
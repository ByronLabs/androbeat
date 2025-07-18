package com.androbeat.androbeatagent.data.actions

import com.androbeat.androbeatagent.data.remote.communication.ActionMessage
import com.androbeat.androbeatagent.data.permissions.DeviceOwnerManager
import com.androbeat.androbeatagent.domain.actions.ActionDispatcherInterface

class ActionDispatcher(
    private val deviceOwnerManager: DeviceOwnerManager
) : ActionDispatcherInterface {

    override fun dispatchAction(action: ActionMessage) {
        when (action.action) {
            "SET_DEVICE_OWNER" -> {
                deviceOwnerManager.setDeviceOwner()
            }
            "CLEAR_DEVICE_OWNER" -> {
                deviceOwnerManager.clearDeviceOwner()
            }
            "LOCK_DEVICE" -> {
                deviceOwnerManager.lockDevice()
            }
            "SET_PASSWORD_MINIMUM_LENGTH" -> {
                deviceOwnerManager.setPasswordMinimumLength(8)
            }
            "WIPE_DATA" -> {
                deviceOwnerManager.wipeData()
            }
            "DISABLE_BLUETOOTH" -> {
                deviceOwnerManager.disableBluetooth()
            }
            "SET_AIRPLANE_MODE" -> {
                deviceOwnerManager.setAirplaneMode()
            }
            "DISABLE_NETWORK" -> {
                deviceOwnerManager.disableMobileData()
                deviceOwnerManager.disableWifi()
            }
            else -> {
                // Handle unknown action
            }
        }
    }
}
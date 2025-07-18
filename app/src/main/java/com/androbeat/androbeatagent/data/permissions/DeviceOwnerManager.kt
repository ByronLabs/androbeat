package com.androbeat.androbeatagent.data.permissions

import android.app.admin.DevicePolicyManager
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.UserManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.communication.UrgentNotificationHelper
import com.androbeat.androbeatagent.presentation.notifications.UrgentNotificationHelperImp
import javax.inject.Inject

class DeviceOwnerManager @Inject constructor(
    private val context: Context,
    private val urgentNotificationHelper: UrgentNotificationHelper
) {
    private val TAG = "DeviceOwnerManager"
    private val devicePolicyManager =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val adminComponent = ComponentName(context, MyDeviceAdminReceiver::class.java)


    fun lockDevice() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            devicePolicyManager.lockNow()
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please lock the phone."
            );
        }
    }

    fun setPasswordMinimumLength(length: Int) {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                devicePolicyManager.requiredPasswordComplexity =
                    DevicePolicyManager.PASSWORD_COMPLEXITY_HIGH
            } else {
                devicePolicyManager.setPasswordMinimumLength(adminComponent, length)
            }
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please re set the phone password"
            );
        }
    }

    fun wipeData() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE)
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please shutdown and wipe your phone ASAP"
            );
        }
    }

    fun setDeviceOwner() {
        if (devicePolicyManager.isDeviceOwnerApp(context.packageName)) {
            Logger.logDebug(TAG, "Already device owner")
        } else {
            Logger.logDebug(TAG, "Setting device owner")
            devicePolicyManager.setDeviceOwnerLockScreenInfo(
                adminComponent,
                "Device owner lock screen info"
            )
        }
    }

    fun clearDeviceOwner() {
        if (devicePolicyManager.isDeviceOwnerApp(context.packageName)) {
            devicePolicyManager.clearDeviceOwnerApp(context.packageName)
        } else {
            Logger.logDebug(TAG, "Not device owner")
        }
    }

    fun disableMobileData() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            try {
                if (devicePolicyManager.isDeviceOwnerApp(context.packageName)) {
                    devicePolicyManager.addUserRestriction(
                        adminComponent,
                        UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS
                    )
                    Logger.logDebug(TAG, "Mobile data disabled")
                } else {
                    Logger.logDebug(TAG, "App is not device owner")
                }
            } catch (e: SecurityException) {
                Logger.logError(TAG, "Permission denied to disable mobile data: ${e.message}")
                urgentNotificationHelper.sendEmergencyNotification(
                    "Emergency Alert",
                    "You don´t have device owner permissions. Please disable all internet connections"
                );
            }
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please disable all internet connections"
            );
        }
    }

    fun disableWifi() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            try {
                if (devicePolicyManager.isDeviceOwnerApp(context.packageName)) {
                    devicePolicyManager.addUserRestriction(
                        adminComponent,
                        UserManager.DISALLOW_CONFIG_WIFI
                    )
                    val wifiManager =
                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val connectivityManager =
                        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        val network =
                            connectivityManager.activeNetwork ?: return
                        val capabilities =
                            connectivityManager.getNetworkCapabilities(network)
                        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                            wifiManager.disconnect()
                        }
                    } else {
                        wifiManager.isWifiEnabled = false
                    }
                    Logger.logDebug(TAG, "WiFi disabled successfully")
                } else {
                    Logger.logDebug(TAG, "App is not device owner")
                }
            } catch (e: SecurityException) {
                Logger.logError(TAG, "Permission denied to disable WiFi: ${e.message}")
                urgentNotificationHelper.sendEmergencyNotification(
                    "Emergency Alert",
                    "You don´t have device owner permissions. Please disable Wifi connection"
                );
            }
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please disable Wifi connection"
            );
        }
    }

    fun disableBluetooth() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.BLUETOOTH_ADMIN
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        bluetoothAdapter.disable()
                        Logger.logDebug(TAG, "Bluetooth disabled")
                    } catch (e: SecurityException) {
                        Logger.logError(TAG, "Permission denied to disable Bluetooth")
                        urgentNotificationHelper.sendEmergencyNotification(
                            "Emergency Alert",
                            "You don´t have device owner permissions. Please disable Bluetooth connexion"
                        );
                    }
                } else {
                    Logger.logDebug(TAG, "BLUETOOTH_ADMIN permission not granted")
                    urgentNotificationHelper.sendEmergencyNotification(
                        "Emergency Alert",
                        "You don´t have device owner permissions. Please disable Bluetooth connexion"
                    );
                }
            } else {
                Logger.logDebug(TAG, "Bluetooth is already disabled or not available")
            }
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please disable Bluetooth connexion"
            );
        }
    }

    fun setAirplaneMode() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            context.startActivity(intent)
        } else {
            Logger.logDebug(TAG, "Device Admin not active")
            urgentNotificationHelper.sendEmergencyNotification(
                "Emergency Alert",
                "You don´t have device owner permissions. Please set airplane mode"
            );
        }
    }
}
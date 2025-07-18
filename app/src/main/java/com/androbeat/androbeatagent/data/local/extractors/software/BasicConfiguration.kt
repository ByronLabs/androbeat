package com.androbeat.androbeatagent.data.local.extractors.software

import android.accounts.AccountManager
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.androbeat.androbeatagent.data.local.extractors.network.WifiConnectedInfo
import com.androbeat.androbeatagent.data.model.models.extractors.software.BasicConfigurationModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType
import java.io.File
import java.util.Locale
import java.util.TimeZone

class BasicConfiguration(private val context: Context) : DataExtractor,
    DataProvider<BasicConfigurationModel?> {
    private val stats = BasicConfigurationModel()

    fun getBasedConfigurationStatistics(): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        stats.apply {
            carrierName = telephonyManager.networkOperatorName
            manufacturer = Build.MANUFACTURER ?: "DEFAULT"
            model = Build.MODEL ?: "DEFAULT"
            isEmulator = checkIsEmulator()
            isRooted = checkIsRooted()
            isRoaming = telephonyManager.isNetworkRoaming
            buildFingerprint = Build.FINGERPRINT
            kernelVersion = getKernelVersion()
            timezone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
            locale = Locale.getDefault().country
            loggedAccounts = getLoggedAccountNames()
        }
        return "Basic configuration {${stats.carrierName} ${stats.model} ${stats.isRoaming}, " +
                "Manufacturer: ${stats.manufacturer} ,Emulator: ${stats.isEmulator}, " +
                "Rooted: ${stats.isRooted} , Fingerprint: ${stats.buildFingerprint}}"
    }

    private fun checkIsEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT ?: return false
        return listOf(
            "generic",
            "unknown",
            "google_sdk",
            "Emulator",
            "emu",
            "Android SDK built for x86",
            "sdk_",
            "Genymotion"
        ).any {
            fingerprint.startsWith(it) || Build.MODEL.contains(it) || Build.MANUFACTURER.contains(it) ||
                    Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                    "google_sdk" == Build.PRODUCT
        }
    }

    private fun getLoggedAccountNames(): List<String> = runCatching {
        AccountManager.get(context).accounts.map { it.name }
    }.getOrElse { emptyList() }

    private fun checkIsRooted(): Boolean = arrayOf(
        "/sbin/",
        "/system/bin/",
        "/system/xbin/",
        "/system/sd/xbin/",
        "/system/usr/we-need-root/su-backup",
        "/system/xbin/mu"
    ).any { File(it + "su").exists() }

    private fun getKernelVersion(): String = runCatching {
        ProcessBuilder("uname", "-r").start().inputStream.bufferedReader().use { it.readLine() }
    }.getOrDefault("")

    override val statistics: String
        get() = getBasedConfigurationStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.BASIC_CONFIGURATION
    override val data: BasicConfigurationModel
        get() = stats

    companion object {
        private val TAG = WifiConnectedInfo::class.java.simpleName
    }

}
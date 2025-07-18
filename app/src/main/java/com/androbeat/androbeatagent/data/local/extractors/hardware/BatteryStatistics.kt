package com.androbeat.androbeatagent.data.local.extractors.hardware

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BatteryModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import java.util.Locale

class BatteryStatistics(private val context: Context) : DataExtractor, DataProvider<BatteryModel> {
    private val batteryModel: BatteryModel = BatteryModel()

    fun getBatteryStatistics(): String {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter) ?: return "Battery Info Unavailable"
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        batteryModel.percentage = level * 100 / scale.toFloat()
        batteryModel.isCharging = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1).let {
            it == BatteryManager.BATTERY_STATUS_CHARGING || it == BatteryManager.BATTERY_STATUS_FULL
        }
        batteryModel.chargingMode = when (batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "WIRELESS"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "DOCK"
            else -> "Unknown"
        }
        batteryModel.voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        batteryModel.temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        batteryModel.health = when (batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            else -> "Unknown"
        }

        val batteryData = "Battery{${batteryModel.isCharging} ${batteryModel.chargingMode} ${"%.4f".format(Locale.getDefault(), batteryModel.percentage)} ${batteryModel.voltage}mV ${batteryModel.temperature / 10.0}°C ${batteryModel.health}}"
        Logger.logDebug(TAG,batteryData)
        return batteryData
    }

    override val statistics: String
        get() = this.getBatteryStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.BATTERY_EXTRACTOR
    override val data: BatteryModel
        get() = batteryModel

    companion object {
        private val TAG = BatteryStatistics::class.java.simpleName
    }
}
package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.DataProvider
import java.util.Locale
import java.util.function.Consumer


class HumiditySensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY, sensorType),
    DataProvider<Float?> {

    var humiditySensorModel = 0f

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            values?.firstOrNull()?.let { humidityValue ->
                humiditySensorModel = humidityValue
                Logger.logInfo(TAG, String.format(Locale.US, "Humidity{%.4f}", humidityValue))
            }
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.HUMIDITY_SENSOR
    override val data: Float
        get() = humiditySensorModel

    companion object {
        private val TAG = HumiditySensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}

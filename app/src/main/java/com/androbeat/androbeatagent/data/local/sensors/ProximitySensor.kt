package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.DataProvider
import java.util.function.Consumer

class ProximitySensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_PROXIMITY, sensorType),
    DataProvider<Boolean?> {

    var proximitySensorModel = false

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            proximitySensorModel = values!![0] == 0f
            Logger.logInfo(TAG, String.format("Proximity{%b}", proximitySensorModel))
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.PROXIMITY_SENSOR
    override val data: Boolean
        get() = proximitySensorModel

    companion object {
        private val TAG = ProximitySensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}
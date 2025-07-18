package com.androbeat.androbeatagent.data.local.sensors


import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.local.BeatService
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import java.util.Locale
import java.util.function.Consumer

class LightSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_LIGHT, sensorType), DataProvider<Float?> {

    var lightSensorModel = 0f

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            lightSensorModel = values?.get(0) ?: 0f
            Logger.logInfo(TAG, String.format(Locale.US,"Light{%.4f}", values?.get(0) ?: 0f))
        }
        register()
    }

    companion object {
        private val TAG = BeatService::class.java.simpleName
    }

    override val type: DataProviderType
        get() = DataProviderType.LIGHT_SENSOR
    override val data: Float
        get() = lightSensorModel

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}
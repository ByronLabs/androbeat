package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.DataProvider
import java.util.Locale
import java.util.function.Consumer


class PressureSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_BAROMETER, sensorType),
    DataProvider<Float?> {

    var pressureSensorModel = 0f

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            pressureSensorModel = values!![0]
            Logger.logInfo(TAG, String.format(Locale.US,"Pressure{%.4f}", values[0]))
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.PRESSURE_SENSOR
    override val data: Float
        get() = pressureSensorModel

    companion object {
        private val TAG = PressureSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}
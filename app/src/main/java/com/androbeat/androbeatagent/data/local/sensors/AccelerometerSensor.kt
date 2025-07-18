package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import java.util.Locale
import java.util.function.Consumer


class AccelerometerSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_ACCELEROMETER, sensorType),
    DataProvider<AxisSensorModel?> {

    var accelerometerSensorModel: AxisSensorModel = AxisSensorModel(0f, 0f, 0f)

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            accelerometerSensorModel = AxisSensorModel(values!![0], values[1], values[2])
            if (BuildConfig.DEBUG_SENSORS) {
                Logger.logInfo(
                    TAG,
                    String.format(Locale.US, "Accelerometer{%.4f %.4f %.4f}", values[0], values[1], values[2])
                )
            }
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.ACCELEROMETER_SENSOR
    override val data: AxisSensorModel
        get() = accelerometerSensorModel

    companion object {
        private val TAG = AccelerometerSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}
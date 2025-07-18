package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import java.util.Locale
import java.util.function.Consumer

class RotationSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_GYROSCOPE, sensorType),
    DataProvider<AxisSensorModel?> {



    var rotationSensorModel: AxisSensorModel = AxisSensorModel(0f, 0f, 0f)

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            if (values != null && values.all { !it.isNaN() }) {
                rotationSensorModel = AxisSensorModel(values[0], values[1], values[2])
                Logger.logInfo(
                    TAG,
                    String.format(Locale.US,"Rotation{%.4f %.4f %.4f}", values[0], values[1], values[2])
                )
            } else {
                rotationSensorModel = AxisSensorModel(0f, 0f, 0f)
            }
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.ROTATION_SENSOR
    override val data: AxisSensorModel
        get() = rotationSensorModel

    companion object {
        private val TAG = RotationSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }

}
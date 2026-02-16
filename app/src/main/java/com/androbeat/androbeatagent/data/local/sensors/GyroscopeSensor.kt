package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import java.util.Locale
import java.util.function.Consumer

class GyroscopeSensor(context: Context?, sensorType: Int) : AndroidSensor(
    context, PackageManager.FEATURE_SENSOR_GYROSCOPE, sensorType
), DataProvider<AxisSensorModel?> {

    var gyroscopeSensorModel: AxisSensorModel = AxisSensorModel(0f, 0f, 0f)

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            if ((values?.size ?: 0) >= 3) {
                val x = values?.getOrNull(0) ?: 0f
                val y = values?.getOrNull(1) ?: 0f
                val z = values?.getOrNull(2) ?: 0f
                gyroscopeSensorModel = AxisSensorModel(x, y, z)
                Logger.logInfo(
                    TAG,
                    String.format(
                        Locale.US,
                        "Gyroscope{%.4f %.4f %.4f}",
                        x,
                        y,
                        z
                    )
                )
            }
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.GYROSCOPE_SENSOR
    override val data: AxisSensorModel
        get() = gyroscopeSensorModel

    companion object {
        private val TAG = GyroscopeSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }
}

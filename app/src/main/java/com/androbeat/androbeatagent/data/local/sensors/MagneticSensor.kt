package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.data.logger.Logger
import java.util.Locale
import java.util.function.Consumer

class MagneticSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_COMPASS, sensorType),
    DataProvider<AxisSensorModel?> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var magenticFieldSensorModel: AxisSensorModel = AxisSensorModel(0f, 0f, 0f)

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            magenticFieldSensorModel =
                AxisSensorModel(values?.get(0) ?: 0f, values?.get(1) ?: 0f, values?.get(2) ?: 0f)
            Logger.logInfo(
                TAG,
                String.format(
                    Locale.US,
                    "Magnetic{%.4f %.4f %.4f}",
                    values?.get(0) ?: 0f,
                    values?.get(1) ?: 0f,
                    values?.get(2) ?: 0f
                )
            )
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.MAGNETIC_SENSOR
    override val data: AxisSensorModel
        get() = magenticFieldSensorModel

    companion object {
        private val TAG = MagneticSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }


}
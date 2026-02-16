package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.DataProvider
import java.util.Locale
import java.util.function.Consumer

class StepsSensor(context: Context?, sensorType: Int) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_STEP_COUNTER, sensorType),
    DataProvider<Float?> {

    var stepsSensorModel = 0f

    init {
        super.setOnSensorValuesChangedListener { values: FloatArray? ->
            values?.firstOrNull()?.let { stepsValue ->
                stepsSensorModel = stepsValue
                Logger.logInfo(TAG, String.format(Locale.US, "Steps{%.4f}", stepsValue))
            }
        }
        register()
    }

    override val type: DataProviderType
        get() = DataProviderType.STEPS_SENSOR
    override val data: Float
        get() = stepsSensorModel

    companion object {
        private val TAG: String = StepsSensor::class.java.simpleName
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getOnSensorValuesChangedListener(): Consumer<FloatArray?>? {
        return super.onSensorValuesChangedListener
    }
}

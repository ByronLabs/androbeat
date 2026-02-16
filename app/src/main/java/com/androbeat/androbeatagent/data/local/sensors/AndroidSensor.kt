package com.androbeat.androbeatagent.data.local.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.androbeat.androbeatagent.domain.sensors.IAndroidSensor
import java.util.function.Consumer

open class AndroidSensor(
    private val context: Context?,
    private val sensorFeature: String?,
    private val sensorType: Int
) : IAndroidSensor, SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    internal var onSensorValuesChangedListener: Consumer<FloatArray?>? = null
    private val isSensorAvailable: Boolean
        private get() = sensorFeature?.let { context?.packageManager?.hasSystemFeature(it) } == true

    override fun register() {
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        sensor = sensorManager?.getDefaultSensor(sensorType)
        if (sensor != null) {
            sensorManager?.registerListener(this, sensor, Int.MAX_VALUE, Int.MAX_VALUE)
        }
    }

    override fun unregister() {
        if (!isSensorAvailable || sensorManager == null) {
            return
        }
        sensorManager?.unregisterListener(this)
    }

    override val power: Float
        get() = if (!isSensorAvailable || sensor == null) {
            (-1).toFloat()
        } else sensor?.power ?: 0f
    override val isWakeUpSensor: Boolean
        get() = if (!isSensorAvailable || sensor == null) {
            false
        } else sensor?.isWakeUpSensor ?: false

     override fun setOnSensorValuesChangedListener(listener: Consumer<FloatArray?>?) {
        onSensorValuesChangedListener = listener
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!isSensorAvailable) {
            return
        }
        if (onSensorValuesChangedListener != null) {
            onSensorValuesChangedListener?.accept(event.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    companion object
}

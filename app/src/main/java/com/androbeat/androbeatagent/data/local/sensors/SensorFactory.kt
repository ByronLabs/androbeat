package com.androbeat.androbeatagent.data.local.sensors


import android.content.Context
import android.hardware.Sensor

object SensorFactory  {
        fun createSensor(context: Context?, sensorType: Int): AndroidSensor? {
            if (context == null) {
                android.util.Log.e("SensorFactory", "Context is null for sensor type $sensorType")
                return null
            }

            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager
            val defaultSensor = sensorManager.getDefaultSensor(sensorType)

            if (defaultSensor == null) {
                android.util.Log.w("SensorFactory", "Sensor type $sensorType NOT AVAILABLE on this device.")
                return null
            } else {
                android.util.Log.i("SensorFactory", "Sensor type $sensorType is available.")
            }

            return when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> {
                    return AccelerometerSensor(context, sensorType)
                }
                Sensor.TYPE_GYROSCOPE -> {
                    return GyroscopeSensor(context, sensorType)
                }
                Sensor.TYPE_LIGHT -> {
                    return LightSensor(context, sensorType)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    return MagneticSensor(context, sensorType)
                }
                Sensor.TYPE_PRESSURE -> {
                    return PressureSensor(context, sensorType)
                }
                Sensor.TYPE_PROXIMITY -> {
                    return ProximitySensor(context, sensorType)
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    return HumiditySensor(context, sensorType)
                }
                Sensor.TYPE_STEP_COUNTER -> {
                    return StepsSensor(context, sensorType)
                }
                Sensor.TYPE_ROTATION_VECTOR -> {
                    return RotationSensor(context, sensorType)
                }
                else -> null
            }
        }
}
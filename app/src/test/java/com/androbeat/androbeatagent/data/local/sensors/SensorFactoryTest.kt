import android.content.Context
import android.hardware.Sensor
import com.androbeat.androbeatagent.data.local.sensors.*
import com.androbeat.androbeatagent.data.repository.managers.SensorManager
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import io.mockk.every
import io.mockk.*
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

class SensorFactoryTest {

    @MockK
    lateinit var context: Context

    @Before
    fun setUp() {
     MockKAnnotations.init(this)
     val sensorManager = mockk<SensorManager>(relaxed = true)
     every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
     val androidSensorManager = mockk<android.hardware.SensorManager>(relaxed = true)
     every { context.getSystemService(Context.SENSOR_SERVICE) } returns androidSensorManager
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun createSensorReturnsAccelerometerSensorForTypeAccelerometer() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_ACCELEROMETER)
        assertTrue(sensor is AccelerometerSensor)
    }

    @Test
    fun createSensorReturnsGyroscopeSensorForTypeGyroscope() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_GYROSCOPE)
        assertTrue(sensor is GyroscopeSensor)
    }

    @Test
    fun createSensorReturnsLightSensorForTypeLight() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_LIGHT)
        assertTrue(sensor is LightSensor)
    }

    @Test
    fun createSensorReturnsMagneticSensorForTypeMagneticField() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_MAGNETIC_FIELD)
        assertTrue(sensor is MagneticSensor)
    }

    @Test
    fun createSensorReturnsPressureSensorForTypePressure() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_PRESSURE)
        assertTrue(sensor is PressureSensor)
    }

    @Test
    fun createSensorReturnsProximitySensorForTypeProximity() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_PROXIMITY)
        assertTrue(sensor is ProximitySensor)
    }

    @Test
    fun createSensorReturnsHumiditySensorForTypeRelativeHumidity() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_RELATIVE_HUMIDITY)
        assertTrue(sensor is HumiditySensor)
    }

    @Test
    fun createSensorReturnsStepsSensorForTypeStepCounter() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_STEP_COUNTER)
        assertTrue(sensor is StepsSensor)
    }

    @Test
    fun createSensorReturnsRotationSensorForTypeRotationVector() {
        val sensor = SensorFactory.createSensor(context, Sensor.TYPE_ROTATION_VECTOR)
        assertTrue(sensor is RotationSensor)
    }

    @Test
    fun createSensorReturnsNullForUnknownSensorType() {
        val sensor = SensorFactory.createSensor(context, -1)
        assertNull(sensor)
    }
}

package com.androbeat.androbeatagent.presentation.tabFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.local.extractors.hardware.BatteryStatistics
import com.androbeat.androbeatagent.data.local.extractors.hardware.BtList
import com.androbeat.androbeatagent.data.local.extractors.hardware.CpuStatistics
import com.androbeat.androbeatagent.data.local.extractors.hardware.RamStatistics
import com.androbeat.androbeatagent.data.local.extractors.network.CellTower
import com.androbeat.androbeatagent.data.local.extractors.network.NetworkStatistics
import com.androbeat.androbeatagent.data.local.extractors.network.WifiConnectedInfo
import com.androbeat.androbeatagent.data.local.extractors.network.WifiList
import com.androbeat.androbeatagent.data.local.extractors.software.AppStatistics
import com.androbeat.androbeatagent.data.local.extractors.software.BasicConfiguration
import com.androbeat.androbeatagent.data.local.extractors.software.EnvironmentVariables
import com.androbeat.androbeatagent.data.local.extractors.software.UserStatistics
import com.androbeat.androbeatagent.data.local.sensors.AccelerometerSensor
import com.androbeat.androbeatagent.data.local.sensors.GyroscopeSensor
import com.androbeat.androbeatagent.data.local.sensors.HumiditySensor
import com.androbeat.androbeatagent.data.local.sensors.LightSensor
import com.androbeat.androbeatagent.data.local.sensors.MagneticSensor
import com.androbeat.androbeatagent.data.local.sensors.PressureSensor
import com.androbeat.androbeatagent.data.local.sensors.ProximitySensor
import com.androbeat.androbeatagent.data.local.sensors.RotationSensor
import com.androbeat.androbeatagent.data.local.sensors.StepsSensor
import com.androbeat.androbeatagent.data.repository.managers.ExtractorManager
import com.androbeat.androbeatagent.data.repository.managers.SensorManager
import javax.inject.Inject

class ServicesFragment @Inject constructor(
    private val sensorManager: SensorManager,
    private val extractorManager: ExtractorManager
) : Fragment() {

    private var servicesList: List<String> = listOf()
    private lateinit var serviceStatus: Map<String, ServiceAvailability>

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter

    private lateinit var sensorClassNames : List<String>
    private lateinit var extractorClassNames : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Create a class to do this automatically when new sensor and extractor is created
        sensorClassNames = listOf(
            AccelerometerSensor::class.java.simpleName,
            GyroscopeSensor::class.java.simpleName,
            LightSensor::class.java.simpleName,
            MagneticSensor::class.java.simpleName,
            PressureSensor::class.java.simpleName,
            ProximitySensor::class.java.simpleName,
            HumiditySensor::class.java.simpleName,
            RotationSensor::class.java.simpleName,
            StepsSensor::class.java.simpleName
        )

        extractorClassNames = listOf(
            NetworkStatistics::class.java.simpleName,
            CpuStatistics::class.java.simpleName,
            AppStatistics::class.java.simpleName,
            BatteryStatistics::class.java.simpleName,
            UserStatistics::class.java.simpleName,
            WifiConnectedInfo::class.java.simpleName,
            WifiList::class.java.simpleName,
            BasicConfiguration::class.java.simpleName,
            CellTower::class.java.simpleName,
            BtList::class.java.simpleName,
            EnvironmentVariables::class.java.simpleName,
            RamStatistics::class.java.simpleName
        )

        servicesList = sensorClassNames + extractorClassNames
        serviceStatus = updateServiceStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_services, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ServiceAdapter(servicesList, serviceStatus)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        serviceStatus = updateServiceStatus()
        adapter.updateServiceStatus(serviceStatus)
    }

    fun updateServiceStatus(): Map<String, ServiceAvailability> {
        val activeSensors = sensorManager.getSensorNames()
        val activeExtractors = extractorManager.getExtractorNames()

        return servicesList.associateWith { service ->
            when {
                activeSensors.contains(service) || activeExtractors.contains(service) -> ServiceAvailability.AVAILABLE
                else -> ServiceAvailability.UNSUPPORTED
            }
        }
    }

}
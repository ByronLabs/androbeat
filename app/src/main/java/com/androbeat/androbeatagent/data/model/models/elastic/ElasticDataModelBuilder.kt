package com.androbeat.androbeatagent.data.model.models.elastic

import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BatteryModel
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BtDeviceModel
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.CpuModel
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.RamModel
import com.androbeat.androbeatagent.data.model.models.extractors.network.CellTowerModel
import com.androbeat.androbeatagent.data.model.models.extractors.network.NetworkModel
import com.androbeat.androbeatagent.data.model.models.extractors.network.WifiModel
import com.androbeat.androbeatagent.data.model.models.extractors.software.BasicConfigurationModel
import com.androbeat.androbeatagent.data.model.models.extractors.software.EnvironmentVariableModel
import com.androbeat.androbeatagent.data.model.models.extractors.software.UserStatisticsModel
import com.androbeat.androbeatagent.data.model.models.sensors.AxisSensorModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.data.enums.DataProviderType
import java.time.Instant

class ElasticDataModelBuilder {
    var elasticDataModel: ElasticDataModel = ElasticDataModel()


    fun build(): ElasticDataModel {
        return elasticDataModel
    }

    fun fromDataProviders(dataProviderList: MutableList<DataProvider<*>>): ElasticDataModel {
        dataProviderList.forEach { dataProvider ->
            when (dataProvider.type) {
                DataProviderType.ACCELEROMETER_SENSOR -> elasticDataModel.accelerometer = dataProvider.data as? AxisSensorModel
                DataProviderType.GYROSCOPE_SENSOR -> elasticDataModel.gyroscope = dataProvider.data as? AxisSensorModel
                DataProviderType.HUMIDITY_SENSOR -> elasticDataModel.humidity = dataProvider.data as? Float
                DataProviderType.LIGHT_SENSOR -> elasticDataModel.light = dataProvider.data as? Float ?: 0f
                DataProviderType.MAGNETIC_SENSOR -> elasticDataModel.magneticSensorValue = dataProvider.data as? AxisSensorModel
                DataProviderType.PRESSURE_SENSOR -> elasticDataModel.pressure = dataProvider.data as? Float
                DataProviderType.PROXIMITY_SENSOR -> elasticDataModel.proximity = dataProvider.data as? Boolean
                DataProviderType.ROTATION_SENSOR -> elasticDataModel.rotation = dataProvider.data as? AxisSensorModel
                DataProviderType.STEPS_SENSOR -> elasticDataModel.steps = dataProvider.data as? Float
                DataProviderType.BATTERY_EXTRACTOR -> elasticDataModel.battery = dataProvider.data as? BatteryModel
                DataProviderType.NETWORK_EXTRACTOR -> elasticDataModel.network = dataProvider.data as? List<NetworkModel>
                DataProviderType.APP_EXTRACTOR -> elasticDataModel.apps = dataProvider.data as? List<String>
                DataProviderType.USER_STATISTICS -> elasticDataModel.userStatistics = dataProvider.data as? List<UserStatisticsModel>
                DataProviderType.CPU_EXTRACTOR -> elasticDataModel.cpu = dataProvider.data as? List<CpuModel>
                DataProviderType.CONNECTED_WIFI -> elasticDataModel.connectedWifi = dataProvider.data as? WifiModel
                DataProviderType.WIFI_LIST -> elasticDataModel.wifiList = dataProvider.data as? List<WifiModel>
                DataProviderType.CELL_TOWER -> elasticDataModel.cellTower = dataProvider.data as? List<CellTowerModel>
                DataProviderType.BASIC_CONFIGURATION -> elasticDataModel.basicConfiguration = dataProvider.data as? BasicConfigurationModel
                DataProviderType.BT_LIST -> elasticDataModel.btList = dataProvider.data as? List<BtDeviceModel>
                DataProviderType.ENV_VARIABLE_LIST -> elasticDataModel.environmentVariables = dataProvider.data as? List<EnvironmentVariableModel>
                DataProviderType.RAM_EXTRACTOR -> elasticDataModel.ramUsage = dataProvider.data as? RamModel //TODO: Find the NULL POINTER HERE
                else -> println("Unhandled data provider type: ${dataProvider.type}")
            }
        }
        elasticDataModel.datetime = Instant.now().toEpochMilli()
        return elasticDataModel
    }
}
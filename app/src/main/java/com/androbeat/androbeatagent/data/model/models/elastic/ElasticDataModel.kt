package com.androbeat.androbeatagent.data.model.models.elastic

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.androbeat.androbeatagent.data.converters.Converters
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

@Entity
class ElasticDataModel : Comparable<ElasticDataModel> {
    @JvmField
    @TypeConverters(Converters::class)
    var accelerometer: AxisSensorModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var gyroscope: AxisSensorModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var light = 0f

    @JvmField
    @TypeConverters(Converters::class)
    var battery: BatteryModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var network: List<NetworkModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var cpu: List<CpuModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var apps: List<String>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var userStatistics: List<UserStatisticsModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var connectedWifi: WifiModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var wifiList: List<WifiModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var basicConfiguration: BasicConfigurationModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var cellTower: List<CellTowerModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var btList: List<BtDeviceModel>? = null

    @JvmField
    @TypeConverters(Converters::class)
    var environmentVariables: List<EnvironmentVariableModel>? = null
    @JvmField
    var isNetworkConnected = true

    @JvmField
    var humidity: Float? = null

    @JvmField
    var magneticSensorValue: AxisSensorModel? = null

    @JvmField
    var pressure: Float? = null

    @JvmField
    var proximity: Boolean? = null

    @JvmField
    @TypeConverters(Converters::class)
    var rotation: AxisSensorModel? = null

    @JvmField
    @TypeConverters(Converters::class)
    var ramUsage: RamModel? = null

    @JvmField
    var steps: Float? = null

    @JvmField
    @PrimaryKey
    var datetime: Long = 0
    fun setEnvironmentVariableList(environmentVariables: List<EnvironmentVariableModel>?) {
        this.environmentVariables = environmentVariables
    }

    override fun compareTo(o: ElasticDataModel): Int {
        return datetime.compareTo(o.datetime)
    }
}
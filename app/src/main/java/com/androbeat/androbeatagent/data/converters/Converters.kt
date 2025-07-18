package com.androbeat.androbeatagent.data.converters


import androidx.room.TypeConverter
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()
    @JvmStatic
    @TypeConverter
    fun fromAxisSensorModel(model: AxisSensorModel?): String {
        return gson.toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun toAxisSensorModel(value: String?): AxisSensorModel {
        return gson.fromJson(value, AxisSensorModel::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun fromBatteryModel(model: BatteryModel?): String {
        return gson.toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun toBatteryModel(value: String?): BatteryModel {
        return gson.fromJson(value, BatteryModel::class.java)
    }

    @TypeConverter
    fun fromNetworkModelList(list: List<NetworkModel?>?): String {
        return gson.toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun toNetworkModelList(value: String?): List<NetworkModel> {
        val listType = object : TypeToken<List<NetworkModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStatisticsModel(list: List<UserStatisticsModel?>?): String {
        return gson.toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun toUserStatisticsModelList(value: String?): List<UserStatisticsModel> {
        val listType = object : TypeToken<List<UserStatisticsModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromCpuModelList(list: List<CpuModel?>?): String {
        return gson.toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun toCpuModelList(value: String?): List<CpuModel> {
        val listType = object : TypeToken<List<CpuModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<String?>?): String {
        return gson.toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @JvmStatic
    @TypeConverter
    fun fromWifiModel(model: WifiModel?): String {
        return gson.toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun toWifiModel(value: String?): WifiModel {
        return gson.fromJson(value, WifiModel::class.java)
    }

    @TypeConverter
    fun fromWifiModelList(list: List<WifiModel?>?): String {
        return gson.toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun fromBasicConfiguration(model: BasicConfigurationModel?): String {
        return gson.toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun toBasicConfigurationModel(value: String?): BasicConfigurationModel {
        return gson.fromJson(value, BasicConfigurationModel::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun toWifiModelList(value: String?): List<WifiModel> {
        val listType = object : TypeToken<List<WifiModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @JvmStatic
    @TypeConverter
    fun toCellTowerList(value: String?): List<CellTowerModel> {
        val listType = object : TypeToken<List<CellTowerModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromCellTowerList(cellTower: List<CellTowerModel?>?): String {
        return gson.toJson(cellTower)
    }

    @JvmStatic
    @TypeConverter
    fun toBtList(value: String?): List<BtDeviceModel> {
        val listType = object : TypeToken<List<BtDeviceModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromBtListList(cellTower: List<BtDeviceModel?>?): String {
        return gson.toJson(cellTower)
    }

    @JvmStatic
    @TypeConverter
    fun toEnvVariableList(value: String?): List<EnvironmentVariableModel> {
        val listType = object : TypeToken<List<EnvironmentVariableModel?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromEnvVariableListList(envVariables: List<EnvironmentVariableModel?>?): String {
        return gson.toJson(envVariables)
    }

    @JvmStatic
    @TypeConverter
    fun fromRamModel(model: RamModel?): String {
        return gson.toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun toRamModel(value: String?): RamModel {
        return gson.fromJson(value, RamModel::class.java)
    }
}
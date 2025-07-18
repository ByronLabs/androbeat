package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModelBuilder
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Instant


class ElasticDataModelBuilderTest {

    @Test
    fun testElasticDataModelBuilder() {
        val builder = ElasticDataModelBuilder()

        // Test build method
        val model = builder.build()
        assertEquals(model, builder.elasticDataModel)
    }


    @Test
    fun fromDataProvidersAssignsValuesCorrectly() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        // Mock data providers for each type
        val accelerometerModel = AxisSensorModel(x = 1.0f, y = 2.0f, z = 3.0f)
        mockDataProviderList.add(object : DataProvider<AxisSensorModel> {
            override val data = accelerometerModel
            override val type = DataProviderType.ACCELEROMETER_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.datetime <= Instant.now().toEpochMilli())
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithGyro() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val accelerometerModel = AxisSensorModel(x = 1.0f, y = 2.0f, z = 3.0f)
        mockDataProviderList.add(object : DataProvider<AxisSensorModel> {
            override val data = accelerometerModel
            override val type = DataProviderType.GYROSCOPE_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)

        assertEquals(accelerometerModel, resultModel.gyroscope)

    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithHumidity() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = 0.0F
        mockDataProviderList.add(object : DataProvider<Float> {
            override val data: Float = 0.0F
            override val type = DataProviderType.HUMIDITY_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.humidity)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithlight() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = 0.0F
        mockDataProviderList.add(object : DataProvider<Float> {
            override val data: Float = 0.0F
            override val type = DataProviderType.LIGHT_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.light)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithMagnetic() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = AxisSensorModel(x = 1.0f, y = 2.0f, z = 3.0f)
        mockDataProviderList.add(object : DataProvider<AxisSensorModel> {
            override val data = data
            override val type = DataProviderType.MAGNETIC_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.magneticSensorValue)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithPressure() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = 0.0F
        mockDataProviderList.add(object : DataProvider<Float> {
            override val data: Float = 0.0F
            override val type = DataProviderType.PRESSURE_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.pressure)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithProximity() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = false
        mockDataProviderList.add(object : DataProvider<Boolean> {
            override val data: Boolean = false
            override val type = DataProviderType.PROXIMITY_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.proximity)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithRotation() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = AxisSensorModel(x = 1.0f, y = 2.0f, z = 3.0f)
        mockDataProviderList.add(object : DataProvider<AxisSensorModel> {
            override val data = data
            override val type = DataProviderType.ROTATION_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.rotation)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithStep() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = 0.0F
        mockDataProviderList.add(object : DataProvider<Float> {
            override val data: Float = 0.0F
            override val type = DataProviderType.STEPS_SENSOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.steps)
    }


    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithBattery() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = BatteryModel()
        mockDataProviderList.add(object : DataProvider<BatteryModel> {
            override val data =data
            override val type = DataProviderType.BATTERY_EXTRACTOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.battery)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithNetwork() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = NetworkModel()
        data.packet= "packet"
        data.sent = 1.0f
        data.received = 2.0f

        mockDataProviderList.add(object : DataProvider<List<NetworkModel>> {
            override val data = listOf(data)
            override val type = DataProviderType.NETWORK_EXTRACTOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)

        assertEquals(data, resultModel.network?.get(0) ?: NetworkModel())
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithApp() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val data = listOf("app")
        mockDataProviderList.add(object : DataProvider<List<String>> {
            override val data: List<String> = listOf("app")
            override val type = DataProviderType.APP_EXTRACTOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(data, resultModel.apps)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithUserStatistics() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val userStatisticsModel = UserStatisticsModel()

        mockDataProviderList.add(object : DataProvider<List<UserStatisticsModel>> {
            override val data = listOf(userStatisticsModel)
            override val type = DataProviderType.USER_STATISTICS
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.userStatistics?.contains(userStatisticsModel) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithCpuExtractor() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val cpuModel = CpuModel()

        mockDataProviderList.add(object : DataProvider<List<CpuModel>> {
            override val data = listOf(cpuModel)
            override val type = DataProviderType.CPU_EXTRACTOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)

        assertTrue(resultModel.cpu?.contains(cpuModel) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithConnectedWifi() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val wifiModel = WifiModel()

        mockDataProviderList.add(object : DataProvider<WifiModel> {
            override val data = wifiModel
            override val type = DataProviderType.CONNECTED_WIFI
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(wifiModel, resultModel.connectedWifi)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithWifiList() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val wifiList = listOf(WifiModel(), WifiModel())

        mockDataProviderList.add(object : DataProvider<List<WifiModel>> {
            override val data = wifiList
            override val type = DataProviderType.WIFI_LIST
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.wifiList?.containsAll(wifiList) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithCellTower() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val cellTowerList = listOf(CellTowerModel(), CellTowerModel())

        mockDataProviderList.add(object : DataProvider<List<CellTowerModel>> {
            override val data = cellTowerList
            override val type = DataProviderType.CELL_TOWER
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.cellTower?.containsAll(cellTowerList) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithBasicConfiguration() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val basicConfigurationModel = BasicConfigurationModel()

        mockDataProviderList.add(object : DataProvider<BasicConfigurationModel> {
            override val data = basicConfigurationModel
            override val type = DataProviderType.BASIC_CONFIGURATION
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(basicConfigurationModel, resultModel.basicConfiguration)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithBtList() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val btList = listOf(BtDeviceModel(), BtDeviceModel())

        mockDataProviderList.add(object : DataProvider<List<BtDeviceModel>> {
            override val data = btList
            override val type = DataProviderType.BT_LIST
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.btList?.containsAll(btList) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithEnvVariableList() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val envList = listOf(
            EnvironmentVariableModel("test","test"),
            EnvironmentVariableModel("test2","test2")
        )

        mockDataProviderList.add(object : DataProvider<List<EnvironmentVariableModel>> {
            override val data = envList
            override val type = DataProviderType.ENV_VARIABLE_LIST
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertTrue(resultModel.environmentVariables?.containsAll(envList) ?: false)
    }

    @Test
    fun fromDataProvidersAssignsValuesCorrectlyWithRamExtractor() {
        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        val ramModel = RamModel()

        mockDataProviderList.add(object : DataProvider<RamModel> {
            override val data = ramModel
            override val type = DataProviderType.RAM_EXTRACTOR
        })

        val resultModel = builder.fromDataProviders(mockDataProviderList)
        assertEquals(ramModel, resultModel.ramUsage)
    }

    @Test
    fun fromDataProvidersHandlesUnhandledDataProviderType() {
        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))

        val builder = ElasticDataModelBuilder()
        val mockDataProviderList = mutableListOf<DataProvider<*>>()

        // Create a mock DataProvider with an unhandled type
        val unhandledDataProvider = object : DataProvider<Any> {
            override val data = Any()
            override val type = null
        }

        mockDataProviderList.add(unhandledDataProvider)
        builder.fromDataProviders(mockDataProviderList)

        val expectedOutput = "Unhandled data provider type: ${unhandledDataProvider.type}"
        assertTrue(outContent.toString().contains(expectedOutput))
    }


}
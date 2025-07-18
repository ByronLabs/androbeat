package com.androbeat.androbeatagent.data


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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertersTest {

    private val converters = Converters
    private val gson = Gson()

    @Test
    fun testAxisSensorModelConversion() {
        val original = AxisSensorModel()
        val converted = converters.toAxisSensorModel(converters.fromAxisSensorModel(original))
        assertTrue(original == converted)
    }

    @Test
    fun testBatteryModelConversion() {
        val original = BatteryModel()
        val converted = converters.toBatteryModel(converters.fromBatteryModel(original))
        assertTrue(original==(converted))
    }

    @Test
    fun testWifiModelConversion() {
        val original = WifiModel()
        val converted = converters.toWifiModel(converters.fromWifiModel(original))
        assertTrue(original==(converted))
    }

    @Test
    fun testBasicConfigurationModelConversion() {
        val original = BasicConfigurationModel()
        val converted = converters.toBasicConfigurationModel(converters.fromBasicConfiguration(original))
        assertTrue(original==(converted))
    }

    @Test
    fun testNetworkModelListConversion() {
        val original = listOf(NetworkModel())
        val converted = gson.fromJson<List<NetworkModel>>(converters.fromNetworkModelList(original), object : TypeToken<List<NetworkModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1==(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testCpuModelListConversion() {
        val original = listOf(CpuModel())
        val converted = gson.fromJson<List<CpuModel>>(converters.fromCpuModelList(original), object : TypeToken<List<CpuModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1==(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testWifiModelListConversion() {
        val original = listOf(WifiModel())
        val converted = gson.fromJson<List<WifiModel>>(converters.fromWifiModelList(original), object : TypeToken<List<WifiModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1==(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testCellTowerModelListConversion() {
        val original = listOf(CellTowerModel())
        val converted = gson.fromJson<List<CellTowerModel>>(converters.fromCellTowerList(original), object : TypeToken<List<CellTowerModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1.equals(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testBtDeviceModelListConversion() {
        val original = listOf(BtDeviceModel())
        val converted = gson.fromJson<List<BtDeviceModel>>(converters.fromBtListList(original), object : TypeToken<List<BtDeviceModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1.equals(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testEnvironmentVariableModelListConversion() {
        val original = listOf(EnvironmentVariableModel("key","value"))
        val converted = gson.fromJson<List<EnvironmentVariableModel>>(converters.fromEnvVariableListList(original), object : TypeToken<List<EnvironmentVariableModel>>() {}.type)
        val areEqual = original.zip(converted).all { (n1, n2) -> n1.equals(n2)  }
        assertTrue(areEqual)
    }

    @Test
    fun testRamModelConversion() {
        val original = RamModel() // replace with actual object
        val converted = converters.toRamModel(converters.fromRamModel(original))
        assertEquals(original, converted)
    }

    @Test
    fun testStringListConversion() {
        val original = listOf("string1", "string2")
        val converted = converters.toStringList(converters.fromStringList(original))
        assertEquals(original, converted)
    }

    @Test
    fun testToCpuModelList() {
        val json = """
    [
        {"minFreq": 1000, "maxFreq": 2000, "curFreq": 1500},
        {"minFreq": 1100, "maxFreq": 2100, "curFreq": 1600}
    ]
    """
        val expected = listOf(
            CpuModel(longArrayOf(1000, 2000, 1500)),
            CpuModel(longArrayOf(1100, 2100, 1600))
        )
        val result = converters.toCpuModelList(json)
        assertEquals(expected, result)
    }

    @Test
    fun testToWifiModelList() {
        val json = """
    [
        {"ssid": "wifi1",  "bssid" = "bssid1" },
        {"ssid": "wifi2", "bssid" = "bssid2" }
    ]
"""
        val expected = listOf(
            WifiModel().apply { ssid = "wifi1"; bssid = "bssid1" },
            WifiModel().apply { ssid = "wifi2"; bssid = "bssid2" }
        )
        val result = converters.toWifiModelList(json)
        assertTrue(expected[1] == result[1])
    }

    @Test
    fun testToCellTowerList() {
        val json = """
    [
        {"mcc" = 1; "mnc" = 1; "lac" = 1; "arfcn" = 1; "bsicPscPci" = 1; "asuLevel" = 1; "signalLevel" = -70; "dbm" = -70; "type" = "cell1"},
        {"mcc" = 2; "mnc" = 2; "lac" = 2; "arfcn" = 2; "bsicPscPci" = 2; "asuLevel" = 2; "signalLevel" = -80; "dbm" = -80; "type" = "cell2"}
    ]
"""
        val expected = listOf(
            CellTowerModel().apply { mcc = 1; mnc = 1; lac = 1; arfcn = 1; bsicPscPci = 1; asuLevel = 1; signalLevel = -70; dbm = -70; type = "cell1" },
            CellTowerModel().apply { mcc = 2; mnc = 2; lac = 2; arfcn = 2; bsicPscPci = 2; asuLevel = 2; signalLevel = -80; dbm = -80; type = "cell2" }
        )
        val result = converters.toCellTowerList(json)
        assertTrue(expected[1].equals(result[1]))
    }

    @Test
    fun testToBtList() {
        val json = """
    [
        {"name": "device1", "address": "00:11:22:33:44:55","alias": "test"},
        {"name": "device2", "address": "66:77:88:99:AA:BB","alias": "test"}
    ]
"""
        val expected = listOf(
            BtDeviceModel().apply { name = "device1"; address = "00:11:22:33:44:55"; alias = "test" },
            BtDeviceModel().apply { name = "device2"; address = "66:77:88:99:AA:BB"; alias = "test" }
        )

        val result = converters.toBtList(json)
        assertTrue(expected[1].equals(result[1]))
    }

    @Test
    fun testToEnvVariableList() {
        val json = """
    [
        {"name": "key1", "value": "value1"},
        {"name": "key2", "value": "value2"}
    ]
    """
        val expected = listOf(
            EnvironmentVariableModel("key1", "value1"),
            EnvironmentVariableModel("key2", "value2")
        )
        val result = converters.toEnvVariableList(json)
        assertTrue(expected[1].equals(result[1]))
    }

    @Test
    fun testToNetworkModelList() {
        val json = """
    [
        {"packet": "value1", "received": 2.0, "sent": 3.0},
        {"packet": "value3", "received": 4.0, "sent": 5.0}
    ]
    """
        val expected = listOf(
            NetworkModel().apply { packet = "value1"; received = 2.0f; sent = 3.0f },
            NetworkModel().apply { packet = "value3"; received = 4.0f; sent = 5.0f }
        )
        val result = Converters.toNetworkModelList(json)
        assertTrue(expected[1] == result[1])
    }

    @Test
    fun testFromStatisticsModel() {
        val list = listOf(
            UserStatisticsModel().apply {
                packet = "value1"
                totalTimeInForeground = 1000L
                lastTimeUsed = 2000L
                describeContents = 1
                appBytes = 3000L
                dataBytes = 4000L
                cacheBytes = 5000L
            },
            UserStatisticsModel().apply {
                packet = "value2"
                totalTimeInForeground = 1100L
                lastTimeUsed = 2100L
                describeContents = 2
                appBytes = 3100L
                dataBytes = 4100L
                cacheBytes = 5100L
            }
        )
        val expectedJson = """
    [{"packet":"value1","totalTimeInForeground":1000,"lastTimeUsed":2000,"describeContents":1,"appBytes":3000,"dataBytes":4000,"cacheBytes":5000},
    {"packet":"value2","totalTimeInForeground":1100,"lastTimeUsed":2100,"describeContents":2,"appBytes":3100,"dataBytes":4100,"cacheBytes":5100}]
    """.trimIndent()
        val result = Converters.fromStatisticsModel(list)
        assertEquals(expectedJson.replace("\\s".toRegex(), ""), result.replace("\\s".toRegex(), ""))
    }


    @Test
    fun testToUserStatisticsModelList() {
        val json = """
    [
        {"packet": "value1", "totalTimeInForeground": 1000, "lastTimeUsed": 2000, "describeContents": 1, "appBytes": 3000, "dataBytes": 4000, "cacheBytes": 5000},
        {"packet": "value2", "totalTimeInForeground": 1100, "lastTimeUsed": 2100, "describeContents": 2, "appBytes": 3100, "dataBytes": 4100, "cacheBytes": 5100}
    ]
    """
        val expected = listOf(
            UserStatisticsModel().apply {
                packet = "value1"
                totalTimeInForeground = 1000L
                lastTimeUsed = 2000L
                describeContents = 1
                appBytes = 3000L
                dataBytes = 4000L
                cacheBytes = 5000L
            },
            UserStatisticsModel().apply {
                packet = "value2"
                totalTimeInForeground = 1100L
                lastTimeUsed = 2100L
                describeContents = 2
                appBytes = 3100L
                dataBytes = 4100L
                cacheBytes = 5100L
            }
        )
        val result = Converters.toUserStatisticsModelList(json)
        assertTrue(expected[1] == result[1])
    }

}
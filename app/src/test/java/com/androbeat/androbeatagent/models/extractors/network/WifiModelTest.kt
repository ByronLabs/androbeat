package com.androbeat.androbeatagent.models.extractors.network

import com.androbeat.androbeatagent.data.model.models.extractors.network.WifiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WifiModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val wifi1 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "00:11:22:33:44:55"
        }
        val wifi2 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "00:11:22:33:44:55"
        }

        assertTrue(wifi1 == wifi2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val wifi1 = WifiModel().apply {
            ssid = "TestSSID1"
            bssid = "00:11:22:33:44:55"
        }
        val wifi2 = WifiModel().apply {
            ssid = "TestSSID2"
            bssid = "66:77:88:99:AA:BB"
        }

        assertFalse(wifi1 == wifi2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val wifi1 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "00:11:22:33:44:55"
        }
        val wifi2 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "00:11:22:33:44:55"
        }

        assertEquals(wifi1.hashCode(), wifi2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val wifi1 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "00:11:22:33:44:55"
        }
        val wifi2 = WifiModel().apply {
            ssid = "TestSSID"
            bssid = "66:77:88:99:AA:BB"
        }

        assertNotEquals(wifi1.hashCode(), wifi2.hashCode())
    }
}
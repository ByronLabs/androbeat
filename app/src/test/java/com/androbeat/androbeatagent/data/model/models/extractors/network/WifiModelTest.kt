package com.androbeat.androbeatagent.data.model.models.extractors.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class WifiModelTest {

    @Test
    fun testEquals() {
        val model1 = WifiModel().apply {
            ssid = "SSID1"
            bssid = "BSSID1"
        }
        val model2 = WifiModel().apply {
            ssid = "SSID1"
            bssid = "BSSID1"
        }
        val model3 = WifiModel().apply {
            ssid = "SSID2"
            bssid = "BSSID2"
        }

        assertEquals(model1, model2)
        assertNotEquals(model1, model3)
    }

    @Test
    fun testHashCode() {
        val model1 = WifiModel().apply {
            ssid = "SSID1"
            bssid = "BSSID1"
        }
        val model2 = WifiModel().apply {
            ssid = "SSID1"
            bssid = "BSSID1"
        }
        val model3 = WifiModel().apply {
            ssid = "SSID2"
            bssid = "BSSID2"
        }

        assertEquals(model1.hashCode(), model2.hashCode())
        assertNotEquals(model1.hashCode(), model3.hashCode())
    }

    @Test
    fun testConstructor() {
        val model = WifiModel().apply {
            ssid = "SSID1"
            bssid = "BSSID1"
        }
        assertEquals("SSID1", model.ssid)
        assertEquals("BSSID1", model.bssid)
    }

    @Test
    fun testDefaultConstructor() {
        val model = WifiModel()
        assertEquals(null, model.ssid)
        assertEquals(null, model.bssid)
    }
}
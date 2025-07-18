package com.androbeat.androbeatagent.models.extractors.hardware

import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BtDeviceModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BtDeviceModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val device1 = BtDeviceModel().apply {
            name = "Device1"
            address = "00:11:22:33:44:55"
            alias = "Alias1"
        }
        val device2 = BtDeviceModel().apply {
            name = "Device1"
            address = "00:11:22:33:44:55"
            alias = "Alias1"
        }

        assertTrue(device1.equals(device2))
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val device1 = BtDeviceModel().apply {
            name = "Device1"
            address = "00:11:22:33:44:55"
            alias = "Alias1"
        }
        val device2 = BtDeviceModel().apply {
            name = "Device2"
            address = "66:77:88:99:AA:BB"
            alias = "Alias2"
        }

        assertFalse(device1.equals(device2))
    }
}
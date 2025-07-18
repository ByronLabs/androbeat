package com.androbeat.androbeatagent.models.extractors.software

import com.androbeat.androbeatagent.data.model.models.extractors.software.BasicConfigurationModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BasicConfigurationModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val config1 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }
        val config2 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }

        assertTrue(config1 == config2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val config1 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }
        val config2 = BasicConfigurationModel().apply {
            carrierName = "DifferentCarrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }

        assertFalse(config1 == config2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val config1 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }
        val config2 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }

        assertEquals(config1.hashCode(), config2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val config1 = BasicConfigurationModel().apply {
            carrierName = "Carrier"
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }
        val config2 = BasicConfigurationModel().apply {
            carrierName = "DifferentCarrier" // Changed value
            manufacturer = "Manufacturer"
            model = "Model"
            isRoaming = false
            isEmulator = false
            isRooted = false
            buildFingerprint = "BuildFingerprint"
            kernelVersion = "KernelVersion"
            timezone = "Timezone"
            locale = "Locale"
            loggedAccounts = listOf("Account1", "Account2")
            deviceId = "DeviceId"
            mainAccountName = "MainAccount"
            clientId = "ClientId"
        }

        assertNotEquals(config1.hashCode(), config2.hashCode())
    }
}
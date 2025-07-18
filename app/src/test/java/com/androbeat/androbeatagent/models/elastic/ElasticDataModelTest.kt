package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.model.models.extractors.software.EnvironmentVariableModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test


class ElasticDataModelTest {

    @Test
    fun testEnvironmentVariablesModification() {
        val model = ElasticDataModel()

        // Test initial state
        assertNull(model.accelerometer)
        assertNull(model.gyroscope)
        assertEquals(0f, model.light)
        assertNull(model.battery)
        assertNull(model.network)
        assertNull(model.cpu)
        assertNull(model.apps)
        assertNull(model.userStatistics)
        assertNull(model.connectedWifi)
        assertNull(model.wifiList)
        assertNull(model.basicConfiguration)
        assertNull(model.cellTower)
        assertNull(model.btList)
        assertNull(model.environmentVariables)
        assertTrue(model.isNetworkConnected)
        assertNull(model.humidity)
        assertNull(model.magneticSensorValue)
        assertNull(model.pressure)
        assertNull(model.proximity)
        assertNull(model.rotation)
        assertNull(model.steps)
        assertEquals(0, model.datetime)

        // Test setting values
        val envVars = listOf(EnvironmentVariableModel("key", "value"))
        model.setEnvironmentVariableList(envVars)
        assertEquals(envVars, model.environmentVariables)
    }

    @Test
    fun testComparison() {
        val model = ElasticDataModel()

        // Test initial state
        assertNull(model.accelerometer)
        assertNull(model.gyroscope)
        assertEquals(0f, model.light)
        assertNull(model.battery)
        assertNull(model.network)
        assertNull(model.cpu)
        assertNull(model.apps)
        assertNull(model.userStatistics)
        assertNull(model.connectedWifi)
        assertNull(model.wifiList)
        assertNull(model.basicConfiguration)
        assertNull(model.cellTower)
        assertNull(model.btList)
        assertNull(model.environmentVariables)
        assertTrue(model.isNetworkConnected)
        assertNull(model.humidity)
        assertNull(model.magneticSensorValue)
        assertNull(model.pressure)
        assertNull(model.proximity)
        assertNull(model.rotation)
        assertNull(model.steps)
        assertEquals(0, model.datetime)

        val model2 = ElasticDataModel()
        model2.datetime = 21
        assertEquals(-1, model.compareTo(model2))
    }
}
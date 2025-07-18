package com.androbeat.androbeatagent.extractors.software

import com.androbeat.androbeatagent.data.local.extractors.software.EnvironmentVariables
import org.junit.Assert.assertTrue
import org.junit.Test

class EnvironmentVariablesTest {

    @Test
    fun testGetEnvironmentVariablesStatistics() {
        // Creating an instance of EnvironmentVariables and calling getEnvironmentVariablesStatistics
        val environmentVariables = EnvironmentVariables()
        val statistics = environmentVariables.getEnvironmentVariablesStatistics()

        // Verifying the results
        assertTrue(statistics.contains("PATH"))
    }
}
package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.StatusResponseData
import com.androbeat.androbeatagent.data.model.models.elastic.StatusResponseVersionData
import org.junit.Assert.assertEquals
import org.junit.Test

class StatusResponseDataTest {

    @Test
    fun testNameIsCorrectlyAssigned() {
        // Arrange
        val name = "testName"
        val statusResponseData = StatusResponseData(name, "", "", StatusResponseVersionData("", "", "", "", "", false, "", "", ""), "")

        // Act & Assert
        assertEquals(name, statusResponseData.name)
    }

    @Test
    fun testClusterNameIsCorrectlyAssigned() {
        // Arrange
        val clusterName = "testClusterName"
        val statusResponseData = StatusResponseData("", clusterName, "", StatusResponseVersionData("", "", "", "", "", false, "", "", ""), "")

        // Act & Assert
        assertEquals(clusterName, statusResponseData.clusterName)
    }

    @Test
    fun testClusterUuidIsCorrectlyAssigned() {
        // Arrange
        val clusterUuid = "testClusterUuid"
        val statusResponseData = StatusResponseData("", "", clusterUuid, StatusResponseVersionData("", "", "", "", "", false, "", "", ""), "")

        // Act & Assert
        assertEquals(clusterUuid, statusResponseData.clusterUuid)
    }

    @Test
    fun testVersionIsCorrectlyAssigned() {
        // Arrange
        val version = StatusResponseVersionData("1.0", "default", "release", "abc123", "2023-01-01", false, "8.0", "1.0", "1.0")
        val statusResponseData = StatusResponseData("", "", "", version, "")

        // Act & Assert
        assertEquals(version, statusResponseData.version)
    }

    @Test
    fun testTaglineIsCorrectlyAssigned() {
        // Arrange
        val tagline = "testTagline"
        val statusResponseData = StatusResponseData("", "", "", StatusResponseVersionData("", "", "", "", "", false, "", "", ""), tagline)

        // Act & Assert
        assertEquals(tagline, statusResponseData.tagline)
    }
}
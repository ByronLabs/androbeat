package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.StatusResponseVersionData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusResponseVersionDataTest {

    @Test
    fun testNumberIsCorrectlyAssigned() {
        val number = "1.0"
        val versionData = StatusResponseVersionData(number, "", "", "", "", false, "", "", "")
        assertEquals(number, versionData.number)
    }

    @Test
    fun testBuildFlavorIsCorrectlyAssigned() {
        val buildFlavor = "default"
        val versionData = StatusResponseVersionData("", buildFlavor, "", "", "", false, "", "", "")
        assertEquals(buildFlavor, versionData.buildFlavor)
    }

    @Test
    fun testBuildTypeIsCorrectlyAssigned() {
        val buildType = "release"
        val versionData = StatusResponseVersionData("", "", buildType, "", "", false, "", "", "")
        assertEquals(buildType, versionData.buildType)
    }

    @Test
    fun testBuildHashIsCorrectlyAssigned() {
        val buildHash = "abc123"
        val versionData = StatusResponseVersionData("", "", "", buildHash, "", false, "", "", "")
        assertEquals(buildHash, versionData.buildHash)
    }

    @Test
    fun testBuildDateIsCorrectlyAssigned() {
        val buildDate = "2023-01-01"
        val versionData = StatusResponseVersionData("", "", "", "", buildDate, false, "", "", "")
        assertEquals(buildDate, versionData.buildDate)
    }

    @Test
    fun testBuildSnapshotIsCorrectlyAssigned() {
        val buildSnapshot = true
        val versionData = StatusResponseVersionData("", "", "", "", "", buildSnapshot, "", "", "")
        assertTrue(buildSnapshot == versionData.buildSnapshot)
    }

    @Test
    fun testLuceneVersionIsCorrectlyAssigned() {
        val luceneVersion = "8.0"
        val versionData =
            StatusResponseVersionData("", "", "", "", "", false, luceneVersion, "", "")
        assertEquals(luceneVersion, versionData.luceneVersion)
    }

    @Test
    fun testMinimumWireCompatibilityVersionIsCorrectlyAssigned() {
        val minimumWireCompatibilityVersion = "1.0"
        val versionData = StatusResponseVersionData("", "", "", "", "", false, "", minimumWireCompatibilityVersion, "")
        assertEquals(minimumWireCompatibilityVersion, versionData.minimumWireCompatibilityVersion)
    }

    @Test
    fun testMinimumIndexCompatibilityVersionIsCorrectlyAssigned() {
        val minimumIndexCompatibilityVersion = "1.0"
        val versionData = StatusResponseVersionData("", "", "", "", "", false, "", "", minimumIndexCompatibilityVersion)
        assertEquals(minimumIndexCompatibilityVersion, versionData.minimumIndexCompatibilityVersion)
    }

}
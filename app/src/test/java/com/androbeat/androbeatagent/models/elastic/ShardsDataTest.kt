package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.ShardsData
import org.junit.Assert.assertEquals
import org.junit.Test

class ShardsDataTest {

    @Test
    fun testShardsDataPropertiesAreCorrectlyAssigned() {
        val total = 10
        val successful = 8
        val failed = 2

        val shardsData = ShardsData(total, successful, failed)

        assertEquals("Total should be correctly assigned", total, shardsData.total)
        assertEquals("Successful should be correctly assigned", successful, shardsData.successful)
        assertEquals("Failed should be correctly assigned", failed, shardsData.failed)
    }

    @Test
    fun testTotalIsCorrectlyAssigned() {

        val expectedTotal = 10
        val shardsData = ShardsData(expectedTotal, 0, 0)

        assertEquals("Total should be correctly assigned", expectedTotal, shardsData.total)
    }

    @Test
    fun testSuccessfulIsCorrectlyAssigned() {

        val expectedSuccessful = 8
        val shardsData = ShardsData(0, expectedSuccessful, 0)

        assertEquals("Successful should be correctly assigned", expectedSuccessful, shardsData.successful)
    }

    @Test
    fun testFailedIsCorrectlyAssigned() {

        val expectedFailed = 2
        val shardsData = ShardsData(0, 0, expectedFailed)

        assertEquals("Failed should be correctly assigned", expectedFailed, shardsData.failed)
    }
}
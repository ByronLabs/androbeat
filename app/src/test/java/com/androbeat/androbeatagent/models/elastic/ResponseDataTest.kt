package com.androbeat.androbeatagent.models.elastic

import com.androbeat.androbeatagent.data.model.models.elastic.ResponseData
import com.androbeat.androbeatagent.data.model.models.elastic.ShardsData
import org.junit.Assert.assertEquals
import org.junit.Test

class ResponseDataTest {

    @Test
    fun testShardsAreCorrectlyAssigned() {
        // Arrange
        val shards = ShardsData(1, 1, 0)
        val responseData = ResponseData(shards, "", "", "", 0, "", 0, 0)

        // Act & Assert
        assertEquals(shards, responseData.shards)
    }

    @Test
    fun testIndexIsCorrectlyAssigned() {
        // Arrange
        val index = "test_index"
        val responseData = ResponseData(ShardsData(0, 0, 0), index, "", "", 0, "", 0, 0)

        // Act & Assert
        assertEquals(index, responseData.index)
    }

    @Test
    fun testTypeIsCorrectlyAssigned() {
        // Arrange
        val type = "test_type"
        val responseData = ResponseData(ShardsData(0, 0, 0), "", type, "", 0, "", 0, 0)

        // Act & Assert
        assertEquals(type, responseData.type)
    }

    @Test
    fun testIdIsCorrectlyAssigned() {
        // Arrange
        val id = "test_id"
        val responseData = ResponseData(ShardsData(0, 0, 0), "", "", id, 0, "", 0, 0)

        // Act & Assert
        assertEquals(id, responseData.id)
    }

    @Test
    fun testVersionIsCorrectlyAssigned() {
        // Arrange
        val version = 1
        val responseData = ResponseData(ShardsData(0, 0, 0), "", "", "", version, "", 0, 0)

        // Act & Assert
        assertEquals(version, responseData.version)
    }

    @Test
    fun testResultIsCorrectlyAssigned() {
        // Arrange
        val result = "test_result"
        val responseData = ResponseData(ShardsData(0, 0, 0), "", "", "", 0, result, 0, 0)

        // Act & Assert
        assertEquals(result, responseData.result)
    }

    @Test
    fun testSeqNoIsCorrectlyAssigned() {
        // Arrange
        val seqNo = 1
        val responseData = ResponseData(ShardsData(0, 0, 0), "", "", "", 0, "", seqNo, 0)

        // Act & Assert
        assertEquals(seqNo, responseData.seqNo)
    }

    @Test
    fun testPrimaryTermIsCorrectlyAssigned() {
        // Arrange
        val primaryTerm = 1
        val responseData = ResponseData(ShardsData(0, 0, 0), "", "", "", 0, "", 0, primaryTerm)

        // Act & Assert
        assertEquals(primaryTerm, responseData.primaryTerm)
    }
}
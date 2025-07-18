package com.androbeat.androbeatagent.models.extractors.software

import com.androbeat.androbeatagent.data.model.models.extractors.software.UserStatisticsModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class UserStatisticsModelTest {

    private fun createUserStatisticsModel(packet: String? = "com.example.app", totalTimeInForeground: Long = 10000, lastTimeUsed: Long = 100000, describeContents: Int = 0, appBytes: Long = 500000, dataBytes: Long = 1000000, cacheBytes: Long = 200000): UserStatisticsModel {
        return UserStatisticsModel().apply {
            this.packet = packet
            this.totalTimeInForeground = totalTimeInForeground
            this.lastTimeUsed = lastTimeUsed
            this.describeContents = describeContents
            this.appBytes = appBytes
            this.dataBytes = dataBytes
            this.cacheBytes = cacheBytes
        }
    }

    @Test
    fun testEquals_DifferentInstances() {
        val model1 = createUserStatisticsModel()
        val model2 = createUserStatisticsModel(packet = "com.example.otherapp")

        assertNotEquals(model1, model2)
    }

    @Test
    fun testHashCode_DifferentInstances() {
        val model1 = createUserStatisticsModel()
        val model2 = createUserStatisticsModel(totalTimeInForeground = 20000)

        assertNotEquals(model1.hashCode(), model2.hashCode())
    }

    @Test
    fun testGettersAndSetters() {
        val model = UserStatisticsModel()
        model.packet = "com.example.test"
        model.totalTimeInForeground = 12345
        model.lastTimeUsed = 67890
        model.describeContents = 1
        model.appBytes = 111111
        model.dataBytes = 222222
        model.cacheBytes = 333333

        assertEquals("com.example.test", model.packet)
        assertEquals(12345, model.totalTimeInForeground)
        assertEquals(67890, model.lastTimeUsed)
        assertEquals(1, model.describeContents)
        assertEquals(111111, model.appBytes)
        assertEquals(222222, model.dataBytes)
        assertEquals(333333, model.cacheBytes)
    }
}
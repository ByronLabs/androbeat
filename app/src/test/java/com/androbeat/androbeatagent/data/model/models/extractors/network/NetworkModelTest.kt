package com.androbeat.androbeatagent.data.model.models.extractors.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NetworkModelTest {

    @Test
    fun testEquals() {
        val model1 = NetworkModel("packet1", 100f, 200f)
        val model2 = NetworkModel("packet1", 100f, 200f)
        val model3 = NetworkModel("packet2", 150f, 250f)

        assertEquals(model1, model2)
        assertNotEquals(model1, model3)
    }

    @Test
    fun testHashCode() {
        val model1 = NetworkModel("packet1", 100f, 200f)
        val model2 = NetworkModel("packet1", 100f, 200f)
        val model3 = NetworkModel("packet2", 150f, 250f)

        assertEquals(model1.hashCode(), model2.hashCode())
        assertNotEquals(model1.hashCode(), model3.hashCode())
    }

    @Test
    fun testConstructor() {
        val model = NetworkModel("packet1", 100f, 200f)
        assertEquals("packet1", model.packet)
        assertEquals(100f, model.received, 0.0f)
        assertEquals(200f, model.sent, 0.0f)
    }

    @Test
    fun testDefaultConstructor() {
        val model = NetworkModel()
        assertEquals(null, model.packet)
        assertEquals(0f, model.received, 0.0f)
        assertEquals(0f, model.sent, 0.0f)
    }
}
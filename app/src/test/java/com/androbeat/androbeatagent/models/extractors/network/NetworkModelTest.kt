package com.androbeat.androbeatagent.models.extractors.network

import com.androbeat.androbeatagent.data.model.models.extractors.network.NetworkModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val network1 = NetworkModel("packet1", 100f, 200f)
        val network2 = NetworkModel("packet1", 100f, 200f)

        assertTrue(network1 == network2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val network1 = NetworkModel("packet1", 100f, 200f)
        val network2 = NetworkModel("packet2", 100f, 200f) // Different packet

        assertFalse(network1 == network2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val network1 = NetworkModel("packet1", 100f, 200f)
        val network2 = NetworkModel("packet1", 100f, 200f)

        assertEquals(network1.hashCode(), network2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val network1 = NetworkModel("packet1", 100f, 200f)
        val network2 = NetworkModel("packet1", 150f, 200f) // Different received value

        assertNotEquals(network1.hashCode(), network2.hashCode())
    }
}
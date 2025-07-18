package com.androbeat.androbeatagent.models.extractors.hardware

import com.androbeat.androbeatagent.data.model.models.extractors.hardware.CpuModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CpuModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val cpu1 = CpuModel(longArrayOf(1000, 2000, 1500))
        val cpu2 = CpuModel(longArrayOf(1000, 2000, 1500))

        assertTrue(cpu1 == cpu2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val cpu1 = CpuModel(longArrayOf(1000, 2000, 1500))
        val cpu2 = CpuModel(longArrayOf(1000, 2500, 1500))

        assertFalse(cpu1 == cpu2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val cpu1 = CpuModel(longArrayOf(1000, 2000, 1500))
        val cpu2 = CpuModel(longArrayOf(1000, 2000, 1500))

        assertEquals(cpu1.hashCode(), cpu2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val cpu1 = CpuModel(longArrayOf(1000, 2000, 1500))
        val cpu2 = CpuModel(longArrayOf(1000, 2500, 1500))

        assertNotEquals(cpu1.hashCode(), cpu2.hashCode())
    }
}
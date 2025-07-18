package com.androbeat.androbeatagent.models.extractors.hardware
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.RamModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RamModelTest {
    @Test
    fun constructorAssignsValuesCorrectly() {
        val availableMemory = 8000L
        val totalMemory = 16000L
        val usedMemory = 8000L
        val ramModel = RamModel(availableMemory = availableMemory, totalMemory = totalMemory, usedMemory = usedMemory)

        assertEquals(availableMemory, ramModel.availableMemory)
        assertEquals(totalMemory, ramModel.totalMemory)
        assertEquals(usedMemory, ramModel.usedMemory)
    }

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val ram1 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)
        val ram2 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)

        assertTrue(ram1 == ram2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val ram1 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)
        val ram2 = RamModel(availableMemory = 4000L, totalMemory = 16000L, usedMemory = 12000L)

        assertFalse(ram1 == ram2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val ram1 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)
        val ram2 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)

        assertEquals(ram1.hashCode(), ram2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val ram1 = RamModel(availableMemory = 8000L, totalMemory = 16000L, usedMemory = 8000L)
        val ram2 = RamModel(availableMemory = 4000L, totalMemory = 16000L, usedMemory = 12000L)

        assertNotEquals(ram1.hashCode(), ram2.hashCode())
    }
}
package com.androbeat.androbeatagent.models.extractors.hardware

import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BatteryModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val model1 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }
        val model2 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }

        assertTrue(model1 == model2)
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val model1 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }
        val model2 = BatteryModel().apply {
            isCharging = false
            chargingMode = "AC"
            percentage = 75f
        }

        assertFalse(model1 == model2)
    }

    @Test
    fun hashCodeWithIdenticalValuesMatches() {
        val model1 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }
        val model2 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }

        assertEquals(model1.hashCode(), model2.hashCode())
    }

    @Test
    fun hashCodeChangesWithDifferentValues() {
        val model1 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 50f
        }
        val model2 = BatteryModel().apply {
            isCharging = true
            chargingMode = "USB"
            percentage = 75f // Different percentage
        }

        assertNotEquals(model1.hashCode(), model2.hashCode())
    }
}
package com.androbeat.androbeatagent.models.extractors.network

import com.androbeat.androbeatagent.data.model.models.extractors.network.CellTowerModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CellTowerModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val tower1 = CellTowerModel().apply {
            mcc = 310
            mnc = 260
            lac = 6183
            arfcn = 128
            bsicPscPci = 95
            asuLevel = 31
            signalLevel = 4
            dbm = -53
            type = "LTE"
        }
        val tower2 = CellTowerModel().apply {
            mcc = 310
            mnc = 260
            lac = 6183
            arfcn = 128
            bsicPscPci = 95
            asuLevel = 31
            signalLevel = 4
            dbm = -53
            type = "LTE"
        }

        assertTrue(tower1.equals(tower2))
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val tower1 = CellTowerModel().apply {
            mcc = 310
            mnc = 260
            lac = 6183
            arfcn = 128
            bsicPscPci = 95
            asuLevel = 31
            signalLevel = 4
            dbm = -53
            type = "LTE"
        }
        val tower2 = CellTowerModel().apply {
            mcc = 310
            mnc = 260
            lac = 6183
            arfcn = 129 // Different ARFCN
            bsicPscPci = 95
            asuLevel = 31
            signalLevel = 4
            dbm = -53
            type = "LTE"
        }

        assertFalse(tower1.equals(tower2))
    }
}
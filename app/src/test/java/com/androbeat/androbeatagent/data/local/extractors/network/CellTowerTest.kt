package com.androbeat.androbeatagent.data.local.extractors.network

import android.content.Context
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityWcdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrengthGsm
import android.telephony.CellSignalStrengthLte
import android.telephony.CellSignalStrengthWcdma
import android.telephony.TelephonyManager
import com.androbeat.androbeatagent.data.model.models.extractors.network.CellTowerModel
import com.androbeat.androbeatagent.data.enums.DataProviderType
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CellTowerTest {

    private lateinit var context: Context
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var cellTower: CellTower

    @Before
    fun setup() {
        context = mockk()
        telephonyManager = mockk()
        cellTower = CellTower(context)

        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
    }

    @Test
    fun testGetCellTowerStatistics_Wcdma() {
        val cellInfoWcdma = mockk<CellInfoWcdma>()
        val cellIdentityWcdma = mockk<CellIdentityWcdma>()
        val cellSignalStrengthWcdma = mockk<CellSignalStrengthWcdma>()

        every { cellInfoWcdma.cellIdentity } returns cellIdentityWcdma
        every { cellInfoWcdma.cellSignalStrength } returns cellSignalStrengthWcdma
        every { cellIdentityWcdma.lac } returns 123
        every { cellIdentityWcdma.mcc } returns 456
        every { cellIdentityWcdma.mnc } returns 789
        every { cellIdentityWcdma.psc } returns 10
        every { cellSignalStrengthWcdma.asuLevel } returns 15
        every { cellSignalStrengthWcdma.level } returns 2
        every { cellSignalStrengthWcdma.dbm } returns -85

        every { telephonyManager.allCellInfo } returns listOf(cellInfoWcdma)

        val expectedStatistics = "Cell tower information : { Cell Tower List: [{WCDMA 123 456 -85 10 2 0 15 },]}"
        assertEquals(expectedStatistics, cellTower.statistics)
    }

    @Test
    fun testDataProviderProperty() {
        assertEquals(cellTower, cellTower.dataProvider)
    }

    @Test
    fun testTypeProperty() {
        assertEquals(DataProviderType.CELL_TOWER, cellTower.type)
    }

    @Test
    fun testDataProperty() {
        val cellInfoWcdma = mockk<CellInfoWcdma>()
        val cellIdentityWcdma = mockk<CellIdentityWcdma>()
        val cellSignalStrengthWcdma = mockk<CellSignalStrengthWcdma>()

        every { cellInfoWcdma.cellIdentity } returns cellIdentityWcdma
        every { cellInfoWcdma.cellSignalStrength } returns cellSignalStrengthWcdma
        every { cellIdentityWcdma.lac } returns 123
        every { cellIdentityWcdma.mcc } returns 456
        every { cellIdentityWcdma.mnc } returns 789
        every { cellIdentityWcdma.psc } returns 10
        every { cellSignalStrengthWcdma.asuLevel } returns 15
        every { cellSignalStrengthWcdma.level } returns 2
        every { cellSignalStrengthWcdma.dbm } returns -85

        every { telephonyManager.allCellInfo } returns listOf(cellInfoWcdma)

        cellTower.getCellTowerStatistics()

        val expectedData = listOf(
            CellTowerModel().apply {
                mcc = 456
                mnc = 789
                lac = 123
                arfcn = 0
                bsicPscPci = 10
                asuLevel = 15
                signalLevel = 2
                dbm = -85
                type = "WCDMA"
            }
        )

        assertTrue(expectedData[0].type == cellTower.data[0]?.type)
    }

    @Test
    fun testGetCellTowerStatistics_Lte() {
        val cellInfoLte = mockk<CellInfoLte>()
        val cellIdentityLte = mockk<CellIdentityLte>()
        val cellSignalStrengthLte = mockk<CellSignalStrengthLte>()

        every { cellInfoLte.cellIdentity } returns cellIdentityLte
        every { cellInfoLte.cellSignalStrength } returns cellSignalStrengthLte
        every { cellIdentityLte.tac } returns 123
        every { cellIdentityLte.mcc } returns 456
        every { cellIdentityLte.mnc } returns 789
        every { cellIdentityLte.pci } returns 10
        every { cellSignalStrengthLte.asuLevel } returns 15
        every { cellSignalStrengthLte.level } returns 2
        every { cellSignalStrengthLte.dbm } returns -85

        every { telephonyManager.allCellInfo } returns listOf(cellInfoLte)

        val expectedStatistics = "Cell tower information : { Cell Tower List: [{LTE 123 456 -85 10 2 0 15 },]}"
        assertEquals(expectedStatistics, cellTower.statistics)
    }

    @Test
    fun testGetCellTowerStatistics_Gsm() {
        val cellInfoGsm = mockk<CellInfoGsm>()
        val cellIdentityGsm = mockk<CellIdentityGsm>()
        val cellSignalStrengthGsm = mockk<CellSignalStrengthGsm>()

        every { cellInfoGsm.cellIdentity } returns cellIdentityGsm
        every { cellInfoGsm.cellSignalStrength } returns cellSignalStrengthGsm
        every { cellIdentityGsm.lac } returns 123
        every { cellIdentityGsm.mcc } returns 456
        every { cellIdentityGsm.mnc } returns 789
        every { cellIdentityGsm.psc } returns 10
        every { cellSignalStrengthGsm.asuLevel } returns 15
        every { cellSignalStrengthGsm.level } returns 2
        every { cellSignalStrengthGsm.dbm } returns -85

        every { telephonyManager.allCellInfo } returns listOf(cellInfoGsm)

        val expectedStatistics =
            "Cell tower information : { Cell Tower List: [{GSM 123 456 -85 10 2 0 15 },]}"
        assertEquals(expectedStatistics, cellTower.statistics)
    }
}


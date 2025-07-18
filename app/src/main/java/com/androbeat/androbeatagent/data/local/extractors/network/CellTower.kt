package com.androbeat.androbeatagent.data.local.extractors.network

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import com.androbeat.androbeatagent.data.model.models.extractors.network.CellTowerModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType

class CellTower(private val context: Context) : DataExtractor,
    DataProvider<List<CellTowerModel?>?> {
    private val _stats = mutableListOf<CellTowerModel>()
    private val stats: List<CellTowerModel>
        get() = _stats
    @SuppressLint("MissingPermission")
    fun getCellTowerStatistics(): String {
        _stats.clear()
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var cellInfoList: List<CellInfo>? = null
        cellInfoList = telephonyManager.allCellInfo
        for (cellInfo in cellInfoList) {
            val cellTowerInformation = bindData(cellInfo)
            if (cellTowerInformation != null) {
                _stats.add(cellTowerInformation)
            }
        }
        val logStringCellTowerInfo = generateStatLogStringBuilder()
        return logStringCellTowerInfo.toString()
    }

    private fun bindData(cellInfo: CellInfo): CellTowerModel? {
        var baseStation: CellTowerModel? = null
        if (cellInfo is CellInfoWcdma) {
            val cellIdentityWcdma = cellInfo.cellIdentity
            baseStation = CellTowerModel()
            baseStation.type = "WCDMA"

            //baseStation.cid = cellIdentityWcdma.cid
            baseStation.lac = cellIdentityWcdma.lac
            baseStation.mcc = cellIdentityWcdma.mcc
            baseStation.mnc = cellIdentityWcdma.mnc
            baseStation.bsicPscPci = cellIdentityWcdma.psc
            if (cellInfo.cellSignalStrength != null) {
                baseStation.asuLevel = cellInfo.cellSignalStrength.asuLevel
                baseStation.signalLevel = cellInfo.cellSignalStrength.level
                baseStation.dbm = cellInfo.cellSignalStrength.dbm
            }
        } else if (cellInfo is CellInfoLte) {
            val cellIdentityLte = cellInfo.cellIdentity
            baseStation = CellTowerModel()
            baseStation.type = "LTE"
            //baseStation.cid = cellIdentityLte.ci
            baseStation.mnc = cellIdentityLte.mnc
            baseStation.mcc = cellIdentityLte.mcc
            baseStation.lac = cellIdentityLte.tac
            baseStation.bsicPscPci = cellIdentityLte.pci
            if (cellInfo.cellSignalStrength != null) {
                baseStation.asuLevel = cellInfo.cellSignalStrength.asuLevel
                baseStation.signalLevel = cellInfo.cellSignalStrength.level
                baseStation.dbm = cellInfo.cellSignalStrength.dbm
            }
        } else if (cellInfo is CellInfoGsm) {
            val cellIdentityGsm = cellInfo.cellIdentity
            baseStation = CellTowerModel()
            baseStation.type = "GSM"
            //baseStation.cid = cellIdentityGsm.cid
            baseStation.lac = cellIdentityGsm.lac
            baseStation.mcc = cellIdentityGsm.mcc
            baseStation.mnc = cellIdentityGsm.mnc
            baseStation.bsicPscPci = cellIdentityGsm.psc
            baseStation.asuLevel = cellInfo.cellSignalStrength.asuLevel
            baseStation.signalLevel = cellInfo.cellSignalStrength.level
            baseStation.dbm = cellInfo.cellSignalStrength.dbm
        }
        return baseStation
    }

    private fun generateStatLogStringBuilder(): StringBuilder {
        val userAppStatsStringBuilder =
            StringBuilder("Cell tower information : { Cell Tower List: [")
        for (stat in stats) {
            userAppStatsStringBuilder.append("{")
                .append(stat.type).append(" ")
                .append(stat.lac).append(" ")
                .append(stat.mcc).append(" ")
                //.append(stat.cid).append(" ")
                .append(stat.dbm).append(" ")
                .append(stat.bsicPscPci).append(" ")
                .append(stat.signalLevel).append(" ")
                .append(stat.arfcn).append(" ")
                .append(stat.asuLevel).append(" ")
                //.append(stat.lat).append(" ")
                //.append(stat.lon).append(" ")
                .append("},")
        }
        userAppStatsStringBuilder.append("]}")
        return userAppStatsStringBuilder
    }

    override val statistics: String
        get() = this.getCellTowerStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.CELL_TOWER
    override val data: List<CellTowerModel?>
        get() = stats

    companion object {
        private val TAG = NetworkStatistics::class.java.simpleName
    }

}
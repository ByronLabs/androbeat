package com.androbeat.androbeatagent.data.model.models.extractors.network

class CellTowerModel {
    var mcc = 0
    var mnc = 0
    var lac = 0
    var arfcn = 0
    var bsicPscPci = 0
    var asuLevel = 0
    var signalLevel = 0
    var dbm = 0
    var type: String? = null

    fun equals(other: CellTowerModel): Boolean {
        return mcc == other.mcc && mnc == other.mnc && lac == other.lac && arfcn == other.arfcn &&
                bsicPscPci == other.bsicPscPci && asuLevel == other.asuLevel && signalLevel == other.signalLevel &&
                dbm == other.dbm && type == other.type
    }
}
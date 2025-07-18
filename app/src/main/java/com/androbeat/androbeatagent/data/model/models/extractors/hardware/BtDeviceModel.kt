package com.androbeat.androbeatagent.data.model.models.extractors.hardware

class BtDeviceModel {
    var name: String? = null
    var address: String? = null
    var alias: String? = null

    fun equals(other: BtDeviceModel): Boolean {
        return name == other.name && address == other.address && alias == other.alias
    }
}
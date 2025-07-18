package com.androbeat.androbeatagent.data.model.models.extractors.software

class BasicConfigurationModel {
    var carrierName: String? = null
    var manufacturer: String? = null
    var model: String? = null
    var isRoaming = false
    var isEmulator = false
    var isRooted = false
    var buildFingerprint: String? = null
    var kernelVersion: String? = null
    var timezone: String? = null
    var locale: String? = null
    var loggedAccounts: List<String>? = null

    var deviceId: String = ""
    var mainAccountName= ""
    var clientId =""
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BasicConfigurationModel

        if (carrierName != other.carrierName) return false
        if (manufacturer != other.manufacturer) return false
        if (model != other.model) return false
        if (isRoaming != other.isRoaming) return false
        if (isEmulator != other.isEmulator) return false
        if (isRooted != other.isRooted) return false
        if (buildFingerprint != other.buildFingerprint) return false
        if (kernelVersion != other.kernelVersion) return false
        if (timezone != other.timezone) return false
        if (locale != other.locale) return false
        if (loggedAccounts != other.loggedAccounts) return false
        if (deviceId != other.deviceId) return false
        if (mainAccountName != other.mainAccountName) return false
        if (clientId != other.clientId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = carrierName?.hashCode() ?: 0
        result = 31 * result + (manufacturer?.hashCode() ?: 0)
        result = 31 * result + (model?.hashCode() ?: 0)
        result = 31 * result + isRoaming.hashCode()
        result = 31 * result + isEmulator.hashCode()
        result = 31 * result + isRooted.hashCode()
        result = 31 * result + (buildFingerprint?.hashCode() ?: 0)
        result = 31 * result + (kernelVersion?.hashCode() ?: 0)
        result = 31 * result + (timezone?.hashCode() ?: 0)
        result = 31 * result + (locale?.hashCode() ?: 0)
        result = 31 * result + (loggedAccounts?.hashCode() ?: 0)
        result = 31 * result + deviceId.hashCode()
        result = 31 * result + mainAccountName.hashCode()
        result = 31 * result + clientId.hashCode()
        return result
    }

}
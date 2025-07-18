package com.androbeat.androbeatagent.data.model.models.extractors.hardware

data class BatteryModel(
    var isCharging: Boolean = false,
    var chargingMode: String? = null,
    var percentage: Float = 0f,
    var voltage: Int = 0,
    var temperature: Int = 0,
    var health: String? = "unknown"
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BatteryModel) return false

        if (isCharging != other.isCharging) return false
        if (chargingMode != other.chargingMode) return false
        if (percentage != other.percentage) return false
        if (voltage != other.voltage) return false
        if (temperature != other.temperature) return false
        if (health != other.health) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isCharging.hashCode()
        result = 31 * result + (chargingMode?.hashCode() ?: 0)
        result = 31 * result + percentage.hashCode()
        result = 31 * result + voltage
        result = 31 * result + temperature
        result = 31 * result + (health?.hashCode() ?: 0)
        return result
    }
}
package com.androbeat.androbeatagent.data.model.models.extractors.hardware

class RamModel(
    var availableMemory: Long = 0L,
    var totalMemory: Long = 0L,
    var usedMemory: Long = 0L
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RamModel

        if (availableMemory != other.availableMemory) return false
        if (totalMemory != other.totalMemory) return false
        if (usedMemory != other.usedMemory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = availableMemory.hashCode()
        result = 31 * result + totalMemory.hashCode()
        result = 31 * result + usedMemory.hashCode()
        return result
    }
}
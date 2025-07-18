package com.androbeat.androbeatagent.data.model.models.extractors.hardware

class CpuModel {
    var minFreq: Long = 0
    var maxFreq: Long = 0
    var curFreq: Long = 0

    constructor()
    constructor(freqs: LongArray) {
        minFreq = freqs[0]
        maxFreq = freqs[1]
        curFreq = freqs[2]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CpuModel

        if (minFreq != other.minFreq) return false
        if (maxFreq != other.maxFreq) return false
        if (curFreq != other.curFreq) return false

        return true
    }

    override fun hashCode(): Int {
        var result = minFreq.hashCode()
        result = 31 * result + maxFreq.hashCode()
        result = 31 * result + curFreq.hashCode()
        return result
    }
}
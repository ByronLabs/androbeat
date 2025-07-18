package com.androbeat.androbeatagent.data.model.models.extractors.network

class NetworkModel {
    var packet: String? = null
    var received = 0f
    var sent = 0f

    constructor()
    constructor(packet: String?, received: Float, sent: Float) {
        this.packet = packet
        this.received = received
        this.sent = sent
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NetworkModel

        if (packet != other.packet) return false
        if (received != other.received) return false
        if (sent != other.sent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packet?.hashCode() ?: 0
        result = 31 * result + received.hashCode()
        result = 31 * result + sent.hashCode()
        return result
    }

}
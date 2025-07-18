package com.androbeat.androbeatagent.data.model.models.sensors

import androidx.room.Entity

@Entity
class AxisSensorModel {
    var x = 0f
    var y = 0f
    var z = 0f

    constructor()
    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AxisSensorModel) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }
}
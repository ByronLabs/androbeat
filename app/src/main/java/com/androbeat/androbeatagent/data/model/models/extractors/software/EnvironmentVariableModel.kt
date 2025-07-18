package com.androbeat.androbeatagent.data.model.models.extractors.software

class EnvironmentVariableModel(var name: String, var value: String){
    fun equals(other: EnvironmentVariableModel): Boolean {
        return name == other.name && value == other.value
    }
}
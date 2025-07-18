package com.androbeat.androbeatagent.data.remote.communication

data class ActionModel(
    val id: Int,
    val message: String
)

data class ActionMessage(
    val action: String,
    val reason: String,
    val code: Int,
    val ratio: Int
)
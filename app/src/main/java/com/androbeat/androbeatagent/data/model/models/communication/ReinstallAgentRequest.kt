package com.androbeat.androbeatagent.data.model.models.communication

data class ReinstallAgentRequest(
    val mainAccountName: String,
    val enrollToken: String,
    val email: String
)
package com.androbeat.androbeatagent.data.model.models

sealed class LoginResult {
    data class Success(val data: Boolean) : LoginResult()
    data object Error : LoginResult()
}
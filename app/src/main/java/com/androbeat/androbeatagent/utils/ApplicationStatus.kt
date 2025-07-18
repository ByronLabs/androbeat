package com.androbeat.androbeatagent.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ApplicationStatus {

    sealed class Status {
        object OK : Status()
        data class Error(val message: String) : Status()
    }

    private val _statusFlow = MutableStateFlow<Status>(Status.OK)
    val statusFlow: StateFlow<Status> = _statusFlow

    fun postOK() {
        _statusFlow.value = Status.OK
    }

    fun postError(message: String) {
        _statusFlow.value = Status.Error(message)
    }
}
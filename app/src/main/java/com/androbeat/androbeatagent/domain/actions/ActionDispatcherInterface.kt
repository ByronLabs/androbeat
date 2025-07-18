package com.androbeat.androbeatagent.domain.actions

import com.androbeat.androbeatagent.data.remote.communication.ActionMessage

interface ActionDispatcherInterface {
    fun dispatchAction(action: ActionMessage)
}
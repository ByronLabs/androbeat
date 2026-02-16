package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenManager: ITokenManager
) {
    fun logout() {
        tokenManager.saveTokens(null, null)
    }
}

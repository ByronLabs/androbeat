package com.androbeat.androbeatagent.data.remote.rest.restApiClient

interface ITokenManager {
    fun getJwt(): String?
    fun getRefreshToken(): String?
    fun saveTokens(jwt: String?, refreshToken: String?)
    fun refreshJwtToken(): Pair<String?, String?>
    fun setEmail(email: String?)
    fun getEmail(): String?
}

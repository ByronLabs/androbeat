package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import okhttp3.Interceptor
import okhttp3.Response

class JwtInterceptor(
    private val tokenManager: ITokenManager,
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val skip = original.header(SKIP_HEADER)?.toBoolean() ?: false
        if (skip || isAuthEndpoint(original.url.encodedPath)) {
            return chain.proceed(original)
        }

        val jwt = tokenManager.getJwt()
        val requestWithAuth = if (jwt.isNullOrBlank() || original.header("Authorization") != null) {
            original
        } else {
            original.newBuilder().addHeader("Authorization", "Bearer $jwt").build()
        }

        val response = chain.proceed(requestWithAuth)
        if (response.code != 401) return response

        val retryCount = requestWithAuth.header(RETRY_HEADER)?.toIntOrNull() ?: 0
        if (retryCount >= MAX_RETRY) {
            response.close()
            sessionManager.logout()
            return chain.proceed(requestWithAuth)
        }

        val (newJwt, _) = tokenManager.refreshJwtToken()
        if (newJwt.isNullOrBlank()) {
            response.close()
            sessionManager.logout()
            return chain.proceed(requestWithAuth)
        }

        response.close()
        val retryRequest = requestWithAuth.newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $newJwt")
            .addHeader(RETRY_HEADER, (retryCount + 1).toString())
            .build()

        return chain.proceed(retryRequest)
    }

    private fun isAuthEndpoint(path: String): Boolean {
        val normalized = path.lowercase()
        return normalized.contains("checktokenstatus") ||
            normalized.contains("registerdevice") ||
            normalized.contains("refreshToken")
    }

    companion object {
        private const val RETRY_HEADER = "X-Jwt-Retry"
        private const val SKIP_HEADER = "X-Skip-Jwt"
        private const val MAX_RETRY = 1
    }
}

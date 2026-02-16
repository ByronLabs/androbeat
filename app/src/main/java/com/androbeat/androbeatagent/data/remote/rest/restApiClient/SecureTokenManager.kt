package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.logger.Logger
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SecureTokenManager(
    context: Context,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) : ITokenManager {

    private val prefs: SharedPreferences = try {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        Logger.logWarning(TAG, "Encrypted prefs unavailable, fallback to regular prefs")
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun getJwt(): String? = prefs.getString(KEY_JWT, null)

    override fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    override fun saveTokens(jwt: String?, refreshToken: String?) {
        prefs.edit().apply {
            putString(KEY_JWT, jwt)
            putString(KEY_REFRESH, refreshToken)
        }.apply()
    }

    override fun setEmail(email: String?) {
        prefs.edit().putString(KEY_EMAIL, email).apply()
    }

    override fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    override fun refreshJwtToken(): Pair<String?, String?> {
        val refresh = getRefreshToken() ?: return Pair(null, null)
        val baseUrl = BuildConfig.BASE_URL.toHttpUrlOrNull() ?: return Pair(null, null)
        val urlBuilder = baseUrl.newBuilder()
            .addPathSegments("api/v2/users/refreshToken")
            .addQueryParameter("refresh_token", refresh)

        getEmail()?.takeIf { it.isNotBlank() }?.let { email ->
            urlBuilder.addQueryParameter("email", email)
        }

        val request = Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build()

        return try {
            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return Pair(null, null)
            }

            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            val newJwt = json.optString("jwt").ifBlank { json.optString("access_token") }
                .ifBlank { json.optString("jwt_token") }
                .ifBlank { "" }
            val newRefresh = json.optString("refresh_token").ifBlank { refresh }

            if (newJwt.isBlank()) {
                Pair(null, null)
            } else {
                saveTokens(newJwt, newRefresh)
                Pair(newJwt, newRefresh)
            }
        } catch (e: Exception) {
            Logger.logError(TAG, "Failed to refresh JWT: ${e.message}")
            Pair(null, null)
        }
    }

    companion object {
        private const val TAG = "SecureTokenManager"
        private const val PREF_NAME = "secure_jwt_prefs"
        private const val KEY_JWT = "jwt_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_EMAIL = "email"
    }
}

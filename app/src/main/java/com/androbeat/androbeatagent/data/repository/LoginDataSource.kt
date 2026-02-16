package com.androbeat.androbeatagent.data.repository


import com.androbeat.androbeatagent.data.model.models.Client
import com.androbeat.androbeatagent.data.model.models.DeviceId
import com.androbeat.androbeatagent.data.model.models.Result
import com.androbeat.androbeatagent.data.model.models.communication.Device
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.ITokenManager
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.utils.ApplicationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val apiService: RestApiInterface,
                                          private val database: AppDatabase,
                                          private val tokenManager: ITokenManager) {

    suspend fun enroll(
        token: String,
        deviceId: String,
        model: String,
        manufacturer: String,
        mainAccountName: String
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val clientId = "$deviceId$token$model"

                database.clientIdDao().insertClientId(Client(1, clientId))
                database.deviceIdDao().insertOrUpdateDeviceId(DeviceId(1, deviceId))
                tokenManager.setEmail(mainAccountName)
                tryRegisterDeviceAndSaveTokens(
                    token,
                    deviceId,
                    model,
                    manufacturer,
                    mainAccountName,
                    clientId
                )
                ApplicationStatus.postOK()

                Result.Success(true)
            } catch (e: Throwable) {
                ApplicationStatus.postError("Error enrolling device: ${e.message}")
                Result.Error(IOException("Error enrolling device", e))
            }
        }
    }

    suspend fun checkRemoteTokenStatus(token: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.checkTokenStatus(token).execute()
                if (response.isSuccessful && response.body().toBoolean()) {
                    Result.Success(true)
                } else {
                    Result.Error(IOException("Error logging in ${response.errorBody()}"))
                }
            } catch (e: Throwable) {
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    private fun tryRegisterDeviceAndSaveTokens(
        token: String,
        deviceId: String,
        model: String,
        manufacturer: String,
        mainAccountName: String,
        clientId: String
    ) {
        try {
            val device = Device(
                token = token,
                deviceId = deviceId,
                model = model,
                manufacturer = manufacturer,
                mainAccountName = mainAccountName,
                clientId = clientId,
                status = 1
            )

            val response = apiService.registerDevice(device).execute()
            val body = response.body().orEmpty()
            if (!response.isSuccessful || body.isBlank() || !body.trim().startsWith("{")) {
                return
            }

            val json = JSONObject(body)
            val jwt = json.optString("jwt_token").ifBlank { json.optString("jwt") }
            val refresh = json.optString("refresh_token")
            if (jwt.isNotBlank() && refresh.isNotBlank()) {
                tokenManager.saveTokens(jwt, refresh)
            }
        } catch (_: Exception) {
            // best effort only; local enrollment remains valid
        }
    }
}

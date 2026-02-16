package com.androbeat.androbeatagent.data.repository


import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.model.models.Client
import com.androbeat.androbeatagent.data.model.models.DeviceId
import com.androbeat.androbeatagent.data.model.models.Result
import com.androbeat.androbeatagent.utils.ApplicationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val database: AppDatabase) {

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
                ApplicationStatus.postOK()

                Result.Success(true)
            } catch (e: Throwable) {
                ApplicationStatus.postError("Error enrolling device: ${e.message}")
                Result.Error(IOException("Error enrolling device", e))
            }
        }
    }

    suspend fun checkTokenStatus(token: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val isValid = token.isNotBlank() && token.trim() == BuildConfig.ENROLLMENT_TOKEN.trim()
                if (isValid) {
                    Result.Success(true)
                } else {
                    Result.Error(IOException("Error logging in: invalid token"))
                }
            } catch (e: Throwable) {
                Result.Error(IOException("Error logging in", e))
            }
        }
    }
}

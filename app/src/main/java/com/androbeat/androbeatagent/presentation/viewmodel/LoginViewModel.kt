package com.androbeat.androbeatagent.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.model.models.LoginResult
import com.androbeat.androbeatagent.data.model.models.Result
import com.androbeat.androbeatagent.data.model.models.TokenModel
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.LoginRepository
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun enroll(context: Context, token: String, model: String,
               manufacturer: String,
               mainAccountName: String) {
        viewModelScope.launch {
            if (token.trim() != BuildConfig.ENROLLMENT_TOKEN.trim()) {
                _loginResult.value = LoginResult.Error
                return@launch
            }

            val adInfo = withContext(Dispatchers.IO) {
                AdvertisingIdClient.getAdvertisingIdInfo(context)
            }
            val deviceId = adInfo.id ?: "unknown_device_id"

            val result = loginRepository.enroll(token, deviceId, model)

            if (result is Result.Success) {
                _loginResult.value = LoginResult.Success(result.data)
            } else {
                _loginResult.value = LoginResult.Error
            }
        }
    }

    fun loginDataChanged(token: String) {
        if (!isUserNameValid(token)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_token)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(token: String): Boolean {
        return token.length > 5
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.tokenDao().insert(TokenModel(token = token))
        }
    }

    suspend fun checkTokenStatus(): Boolean {
        val token = appDatabase.tokenDao().getToken()?.token ?: ""
        return withContext(Dispatchers.IO) {
            loginRepository.checkRemoteTokenStatus(token) is Result.Success
        }
    }

    suspend fun isTokenExists(): Boolean {
        return withContext(Dispatchers.IO) {
            appDatabase.tokenDao().getToken() != null
        }
    }
}
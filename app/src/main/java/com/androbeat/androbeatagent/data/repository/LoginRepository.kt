package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.model.models.LoggedInUser
import com.androbeat.androbeatagent.data.model.models.Result
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
) {

    private var user: LoggedInUser? = null

    suspend fun enroll(token: String, deviceId: String, model: String): Result<Boolean> {
        return dataSource.enroll(token, deviceId, model)
    }

    suspend fun checkRemoteTokenStatus(token: String): Result<Boolean> {
        return dataSource.checkRemoteTokenStatus(token)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}
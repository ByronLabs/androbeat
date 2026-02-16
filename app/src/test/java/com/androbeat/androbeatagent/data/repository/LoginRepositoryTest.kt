// LoginRepositoryTest.kt
package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.model.models.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginRepositoryTest {

    private lateinit var loginRepository: LoginRepository
    private lateinit var loginDataSource: LoginDataSource

    @Before
    fun setUp() {
        loginDataSource = mockk()
        loginRepository = LoginRepository(loginDataSource)
    }

    @Test
    fun testEnrollSuccess() = runBlocking {
        val token = "test_token"
        val deviceId = "test_device_id"
        val model = "test_model"
        val manufacturer = "test_manufacturer"
        val mainAccountName = "test@example.com"
        val response = Result.Success(true)

        coEvery {
            loginDataSource.enroll(token, deviceId, model, manufacturer, mainAccountName)
        } returns response

        val result = loginRepository.enroll(token, deviceId, model, manufacturer, mainAccountName)

        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
        coVerify { loginDataSource.enroll(token, deviceId, model, manufacturer, mainAccountName) }
    }

    @Test
    fun testEnrollFailure() = runBlocking {
        val token = "test_token"
        val deviceId = "test_device_id"
        val model = "test_model"
        val manufacturer = "test_manufacturer"
        val mainAccountName = "test@example.com"
        val response = Result.Error(Exception("Error"))

        coEvery {
            loginDataSource.enroll(token, deviceId, model, manufacturer, mainAccountName)
        } returns response

        val result = loginRepository.enroll(token, deviceId, model, manufacturer, mainAccountName)

        assertTrue(result is Result.Error)
        coVerify { loginDataSource.enroll(token, deviceId, model, manufacturer, mainAccountName) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

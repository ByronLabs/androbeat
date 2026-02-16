package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.daos.DeviceIdDao
import com.androbeat.androbeatagent.data.model.models.Result
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.ITokenManager
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class LoginDataSourceTest {

    private lateinit var loginDataSource: LoginDataSource
    private lateinit var apiService: RestApiInterface
    private lateinit var database: AppDatabase
    private lateinit var clientIdDao: ClientIdDao
    private lateinit var deviceIdDao: DeviceIdDao
    private lateinit var tokenManager: ITokenManager

    @Before
    fun setUp() {
        apiService = mockk()
        database = mockk()
        clientIdDao = mockk(relaxed = true)
        deviceIdDao = mockk(relaxed = true)
        tokenManager = mockk(relaxed = true)

        every { database.clientIdDao() } returns clientIdDao
        every { database.deviceIdDao() } returns deviceIdDao

        loginDataSource = LoginDataSource(apiService, database, tokenManager)
    }

    @Test
    fun testEnrollSuccess() = runBlocking {
        val registerCall = mockk<Call<String>>()
        every { apiService.registerDevice(any()) } returns registerCall
        every { registerCall.execute() } returns Response.success("{\"jwt_token\":\"jwt\",\"refresh_token\":\"refresh\"}")
        every { tokenManager.setEmail(any()) } just Runs

        val result = loginDataSource.enroll(
            "test_token",
            "test_device_id",
            "test_model",
            "test_manufacturer",
            "test@example.com"
        )

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { clientIdDao.insertClientId(any()) }
        coVerify(exactly = 1) { deviceIdDao.insertOrUpdateDeviceId(any()) }
        verify(exactly = 1) { apiService.registerDevice(any()) }
        verify(exactly = 1) { tokenManager.setEmail("test@example.com") }
    }

    @Test
    fun testCheckRemoteTokenStatusSuccess() = runBlocking {
        val call = mockk<Call<String>>()
        every { apiService.checkTokenStatus("test_token") } returns call
        every { call.execute() } returns Response.success("true")

        val result = loginDataSource.checkRemoteTokenStatus("test_token")

        assertTrue(result is Result.Success)
    }

    @Test
    fun testCheckRemoteTokenStatusFailure() = runBlocking {
        val call = mockk<Call<String>>()
        every { apiService.checkTokenStatus("test_token") } returns call
        every { call.execute() } returns Response.error(400, "Error".toResponseBody(null))

        val result = loginDataSource.checkRemoteTokenStatus("test_token")

        assertTrue(result is Result.Error)
    }

    @Test
    fun testCheckRemoteTokenStatusException() = runBlocking {
        val call = mockk<Call<String>>()
        every { apiService.checkTokenStatus("test_token") } returns call
        every { call.execute() } throws RuntimeException("Network error")

        val result = loginDataSource.checkRemoteTokenStatus("test_token")

        assertTrue(result is Result.Error)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

// LoginDataSourceTest.kt
package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.daos.DeviceIdDao
import com.androbeat.androbeatagent.data.model.models.Result
import com.androbeat.androbeatagent.data.model.models.communication.Device
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response


class LoginDataSourceTest {

    private lateinit var loginDataSource: LoginDataSource
    private lateinit var database: AppDatabase
    private lateinit var clientIdDao: ClientIdDao
    private lateinit var deviceIdDao: DeviceIdDao

    @Before
    fun setUp() {
        database = mockk()
        database = mockk()
        clientIdDao = mockk()
        deviceIdDao = mockk()

        every { database.clientIdDao() } returns clientIdDao
        every { database.deviceIdDao() } returns deviceIdDao

    }

    @Test
    fun testEnrollSuccess(): Unit = runBlocking {
        val device = Device(
            token = "test_token",
            deviceId = "test_device_id",
            model = "test_model",
            mainAccountName = "test_main_account",
            manufacturer = "test_manufacturer",
            clientId = "test_device_idtest_tokentest_model"
        )

        coEvery { apiService.registerDevice(any()).execute() } returns Response.success("true")
        coEvery { database.deviceIdDao().insertOrUpdateDeviceId(any()) } returns Unit
        coEvery { clientIdDao.insertClientId(any()) } just Runs
        coEvery { deviceIdDao.insertDeviceId(any()) } just Runs

        val result = loginDataSource.enroll("test_token", "test_device_id",
            "test_model")

        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)

    }

    @Test
    fun testEnrollFailure() = runBlocking {
        val device = Device(
            token = "test_token",
            deviceId = "test_device_id",
            model = "test_model",
            mainAccountName = "test_main_account",
            manufacturer = "test_manufacturer",
            clientId = "test_device_idtest_tokentest_model"
        )
        val response = Response.error<String>(400, "Error".toResponseBody(null))

        coEvery { apiService.registerDevice(device).execute() } returns response
        val result = loginDataSource.enroll("test_token", "test_device_id",
            "test_model", "test_manufacturer", "test_main_account")

        assertTrue(result is Result.Error)
        coVerify { apiService.registerDevice(device).execute() }
    }

    @Test
    fun testEnrollException() = runBlocking {
        val device = Device(
            token = "test_token",
            deviceId = "test_device_id",
            model = "test_model",
            mainAccountName = "test_main_account",
            manufacturer = "test_manufacturer",
            clientId = "test_device_idtest_tokentest_model"
        )

        coEvery { apiService.registerDevice(device).execute() } throws IOException("Network error")

        val result = loginDataSource.enroll("test_token", "test_device_id",
            "test_model", "test_manufacturer", "test_main_account")

        assertTrue(result is Result.Error)
        coVerify { apiService.registerDevice(device).execute() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
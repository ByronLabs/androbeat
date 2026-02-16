package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.daos.DeviceIdDao
import com.androbeat.androbeatagent.data.model.models.Result
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginDataSourceTest {

    private lateinit var loginDataSource: LoginDataSource
    private lateinit var database: AppDatabase
    private lateinit var clientIdDao: ClientIdDao
    private lateinit var deviceIdDao: DeviceIdDao

    @Before
    fun setUp() {
        database = mockk()
        clientIdDao = mockk(relaxed = true)
        deviceIdDao = mockk(relaxed = true)

        every { database.clientIdDao() } returns clientIdDao
        every { database.deviceIdDao() } returns deviceIdDao

        loginDataSource = LoginDataSource(database)
    }

    @Test
    fun testEnrollSuccess() = runBlocking {
        val result = loginDataSource.enroll(
            BuildConfig.ENROLLMENT_TOKEN,
            "test_device_id",
            "test_model",
            "test_manufacturer",
            "test@example.com"
        )

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { clientIdDao.insertClientId(any()) }
        coVerify(exactly = 1) { deviceIdDao.insertOrUpdateDeviceId(any()) }
    }

    @Test
    fun testCheckTokenStatusSuccess() = runBlocking {
        val result = loginDataSource.checkTokenStatus(BuildConfig.ENROLLMENT_TOKEN)
        assertTrue(result is Result.Success)
    }

    @Test
    fun testCheckTokenStatusFailure() = runBlocking {
        val result = loginDataSource.checkTokenStatus("invalid_token")
        assertTrue(result is Result.Error)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

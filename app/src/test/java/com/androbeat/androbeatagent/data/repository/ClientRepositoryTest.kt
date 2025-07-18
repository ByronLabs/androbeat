// ClientRepositoryTest.kt
package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.model.models.Client
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


class ClientRepositoryTest {

    private lateinit var clientRepository: ClientRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var clientIdDao: ClientIdDao

    @Before
    fun setUp() {
        appDatabase = mockk()
        clientIdDao = mockk()
        every { appDatabase.clientIdDao() } returns clientIdDao
        clientRepository = ClientRepository(appDatabase)
    }

    @Test
    fun testGetClientId() = runBlocking {
        val client = Client(1,"test_id")
        coEvery { clientIdDao.getClientId() } returns client

        val result = clientRepository.getClientId()

        assertEquals(client, result)
        coVerify { clientIdDao.getClientId() }
    }

    @Test
    fun testGetClientIdReturnsNull() = runBlocking {
        coEvery { clientIdDao.getClientId() } returns null

        val result = clientRepository.getClientId()

        assertNull(result)
        coVerify { clientIdDao.getClientId() }
    }

    @Test
    fun testSaveClientId() = runBlocking {
        val client = Client(1,"test_id")
        coEvery { clientIdDao.insertClientId(client) } just Runs

        clientRepository.saveClientId(client)

        coVerify { clientIdDao.insertClientId(client) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
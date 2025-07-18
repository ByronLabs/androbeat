// NetworkHandlerTest.kt
package com.androbeat.androbeatagent.data.repository

import com.androbeat.androbeatagent.data.daos.ElasticDataDao
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class NetworkHandlerTest {

    private lateinit var networkHandler: NetworkHandler
    private lateinit var appDatabase: AppDatabase
    private lateinit var dataManager: DataManagerImp
    private lateinit var elasticDataDao: ElasticDataDao

    @Before
    fun setUp() {
        appDatabase = mockk()
        dataManager = mockk(relaxed = true)
        elasticDataDao = mockk()
        every { appDatabase.elasticDataDao() } returns elasticDataDao
        networkHandler = NetworkHandler(appDatabase, dataManager)

    }

    @Test
    fun testProcessPendingData() = runBlocking {
        val dataList = listOf(
            ElasticDataModel(), ElasticDataModel()
        )
        every { elasticDataDao.all } returns dataList

        networkHandler.processPendingData()

        dataList.forEach { data ->
            verify { dataManager.saveDataOnElasticSearch(data, true) }
        }
    }

    @Test
    fun testProcessPendingDataWithException() = runBlocking {
        every { elasticDataDao.all } throws Exception("Database error")

        networkHandler.processPendingData()

        verify { dataManager wasNot Called }
    }

    @Test
    fun testProcessPendingDataSuccess() = runBlocking {
        val dataList = listOf(
            ElasticDataModel(), ElasticDataModel()
        )
        every { elasticDataDao.all } returns dataList

        networkHandler.processPendingData()

        dataList.forEach { data ->
            verify { dataManager.saveDataOnElasticSearch(data, true) }
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
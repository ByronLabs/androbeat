package com.androbeat.androbeatagent.data.repository.managers

import android.content.Context
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.domain.data.DataProvider
import io.mockk.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService

class DataProcessingManagerTest {

    private lateinit var dataProcessingManager: DataProcessingManager
    private lateinit var dataManager: DataManagerImp
    private lateinit var extractorManager: ExtractorManager
    private lateinit var sensorManager: SensorManager

    private val mockExtractorProvider1: DataProvider<String> = mockk()
    private val mockExtractorProvider2: DataProvider<String> = mockk()
    private val mockSensorProvider1: DataProvider<String> = mockk()
    private val mockSensorProvider2: DataProvider<String> = mockk()

    private lateinit var mockContext: Context
    private lateinit var mockRoomExecutor: ExecutorService
    private lateinit var mockLogstashApiInterface: LogstashApiInterface
    private lateinit var mockAppDatabase: AppDatabase
    private lateinit var mockDateFormatter: SimpleDateFormat

    @Before
    fun setUp() {
        mockContext = mockk()
        mockRoomExecutor = mockk()
        mockLogstashApiInterface = mockk(relaxed = true)
        mockAppDatabase = mockk()
        mockDateFormatter = mockk()

        dataManager = spyk(DataManagerImp(
            context = mockContext,
            roomExecutor = mockRoomExecutor,
            logstashApiInterface = mockLogstashApiInterface,
            appDatabase = mockAppDatabase,
            dateFormatter = mockDateFormatter
        ))
        extractorManager = mockk(relaxed = true)
        sensorManager = mockk(relaxed = true)
        dataProcessingManager = DataProcessingManager(dataManager, extractorManager, sensorManager)

    }

    @Test
    fun testStartDataProcessingCoroutine() = runBlocking {
        coEvery { dataManager.saveDataOnElasticSearch() } just Runs
        coEvery { extractorManager.extractors } returns listOf(mockk(relaxed = true))

        val job = launch {
            dataProcessingManager.startDataProcessingCoroutine()
        }

        delay(1000) // Let the coroutine run for a while
        job.cancelAndJoin()

        coVerify(atLeast = 1) { dataManager.saveDataOnElasticSearch() }
    }

    @Test
    fun testCreateExtractors() {
        dataProcessingManager.createExtractors()

        verify { extractorManager.createExtractors() }
    }

    @Test
    fun testCreateSensors() {
        dataProcessingManager.createSensors()

        verify { sensorManager.createSensors() }
    }

    @Test
    fun testUnregisterSensors() {
        dataProcessingManager.unregisterSensors()
        verify { sensorManager.unregisterSensors() }
    }

    @Test
    fun `test registerProviders when providers are successfully registered`() {
        val mockExtractorProviders = listOf(mockExtractorProvider1, mockExtractorProvider2)
        val mockSensorProviders = listOf(mockSensorProvider1, mockSensorProvider2)

        every { extractorManager.getProviders() } returns mockExtractorProviders
        every { sensorManager.getProviders() } returns mockSensorProviders

        dataProcessingManager.registerProviders()

        assertEquals(4, dataManager._providers.size)
        assertTrue(dataManager._providers.containsAll(mockExtractorProviders))
        assertTrue(dataManager._providers.containsAll(mockSensorProviders))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

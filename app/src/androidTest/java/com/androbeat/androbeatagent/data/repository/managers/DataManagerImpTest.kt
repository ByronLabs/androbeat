package com.androbeat.androbeatagent.data.repository.managers

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.daos.DeviceIdDao
import com.androbeat.androbeatagent.data.daos.ElasticDataDao
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.Client
import com.androbeat.androbeatagent.data.model.models.DeviceId
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel
import com.androbeat.androbeatagent.data.model.models.extractors.software.BasicConfigurationModel
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.domain.data.DataProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.isAccessible

class DataManagerImpTest {

    private lateinit var context: Context
    private lateinit var roomExecutor: ExecutorService
    private lateinit var logstashApiInterface: LogstashApiInterface
    private lateinit var appDatabase: AppDatabase
    private lateinit var dateFormatter: SimpleDateFormat
    private lateinit var dataManagerImp: DataManagerImp
    private lateinit var logger: Logger


    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        roomExecutor = mockk()
        logstashApiInterface = mockk(relaxed = true)
        appDatabase = mockk()
        dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        logger = mockk(relaxed = true)

        val deviceIdDao = mockk<DeviceIdDao>()
        val clientIdDao = mockk<ClientIdDao>()
        val elasticDataDao = mockk<ElasticDataDao>()
        every { appDatabase.deviceIdDao() } returns deviceIdDao
        every { appDatabase.clientIdDao() } returns clientIdDao
        every { appDatabase.elasticDataDao() } returns elasticDataDao
        coEvery { deviceIdDao.getDeviceId() } returns DeviceId(1, "mockedDeviceId")
        coEvery { clientIdDao.getClientId() } returns Client(1, "mockedClientId")
        coEvery { elasticDataDao.insert(any()) } just Runs

        every { roomExecutor.execute(any()) } answers {
            val runnable = it.invocation.args[0] as Runnable
            runnable.run()
        }

        coEvery { logstashApiInterface.sendToLogstash(any()) } returns Response.success(Unit)

        dataManagerImp =
            DataManagerImp(context, roomExecutor, logstashApiInterface, appDatabase, dateFormatter).apply {
            }


    }

    @Test
    fun testSaveDataOnElasticSearch() {
        val data = ElasticDataModel()
        coEvery { logstashApiInterface.sendToLogstash(any()) } returns Response.success(Unit)
        dataManagerImp.saveDataOnElasticSearch(data, false)
        coVerify { logstashApiInterface.sendToLogstash(any()) }
    }

    @Test
    fun testSaveDataOnElasticSearchWithoutParams() {
        val dataProvider = mockk<DataProvider<*>>(relaxed = true)
        dataManagerImp._providers.add(dataProvider)
        coEvery { logstashApiInterface.sendToLogstash(any()) } returns Response.success(Unit)
        dataManagerImp.saveDataOnElasticSearch()
        coVerify { logstashApiInterface.sendToLogstash(any()) }
    }

    @Test
    fun testCurrentDateInDesiredFormat() {
        val date = Date()
        val expectedFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = expectedFormat.format(date)
        assertEquals(formattedDate, dataManagerImp.currentDateInDesiredFormat)
    }

    @Test
    fun testFillIdentificationInfoWithReflectionShouldFillBasicConfiguration() = runBlocking {
        val data = ElasticDataModel().apply {
            basicConfiguration = BasicConfigurationModel()
        }

        // Usa reflection para acceder al método private suspend
        val method = dataManagerImp::class
            .declaredFunctions
            .first { it.name == "fillIdentificationInfo" }

        method.isAccessible = true

        val result = method.callSuspend(dataManagerImp, context, data) as ElasticDataModel

        val basic = result.basicConfiguration!!
        assertEquals("mockedDeviceId", basic.deviceId)
        assertEquals("mockedClientId", basic.clientId)
        assertNotNull(basic.mainAccountName)
    }


}

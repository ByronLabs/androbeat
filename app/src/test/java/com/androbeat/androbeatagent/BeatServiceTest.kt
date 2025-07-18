// BeatServiceTest.kt
package com.androbeat.androbeatagent

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.PowerManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.local.BeatService
import com.androbeat.androbeatagent.data.local.sensors.AndroidSensor
import com.androbeat.androbeatagent.data.local.sensors.SensorFactory
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.domain.communication.NotificationHelper
import com.androbeat.androbeatagent.domain.data.LogReader
import com.androbeat.androbeatagent.presentation.viewmodel.BeatServiceViewModel
import com.androbeat.androbeatagent.utils.ServiceUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
class BeatServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var beatDataSource: BeatDataSource

    @MockK
    lateinit var logReader: LogReader

    @Inject
    lateinit var beatService: BeatService

    @MockK
    lateinit var mHandler: Handler

    @Before
    fun setUp() {

        MockKAnnotations.init(this)
        hiltRule.inject()
        beatDataSource = mockk(relaxed = true)
        logReader = mockk(relaxed = true)
        mHandler = mockk(relaxed = true)
        val viewModel = mockk<BeatServiceViewModel>(relaxed = true)
        val appDatabase = mockk<AppDatabase>(relaxed = true)
        val clientIdDao = mockk<ClientIdDao>(relaxed = true)
        val clientIdLiveData = mockk<MutableLiveData<String>>(relaxed = true)
        val notificationHelper = mockk<NotificationHelper>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val powerManager = mockk<PowerManager>(relaxed = true)
        val wakeLock = mockk<PowerManager.WakeLock>(relaxed = true)
        val sensorFactory = mockk<SensorFactory>(relaxed = true)
        val mockSensor = mockk<AndroidSensor>(relaxed = true)

        every { appDatabase.clientIdDao() } returns clientIdDao

        beatService = spyk(BeatService(), recordPrivateCalls = true)
        beatService.beatDataSource = beatDataSource

        val logServiceField = BeatDataSource::class.java.getDeclaredField("logReader")
        logServiceField.isAccessible = true
        logServiceField.set(beatDataSource, logReader)

        val viewModelField = BeatDataSource::class.java.getDeclaredField("viewModel")
        viewModelField.isAccessible = true
        viewModelField.set(beatDataSource, viewModel)

        val appDatabaseField = BeatServiceViewModel::class.java.getDeclaredField("appDatabase")
        appDatabaseField.isAccessible = true
        appDatabaseField.set(viewModel, appDatabase)

        val clientIdLiveDataField = BeatServiceViewModel::class.java.getDeclaredField("_clientId")
        clientIdLiveDataField.isAccessible = true
        clientIdLiveDataField.set(viewModel, clientIdLiveData)

        val notificationHelperField = BeatDataSource::class.java.getDeclaredField("notificationHelper")
        notificationHelperField.isAccessible = true
        notificationHelperField.set(beatDataSource, notificationHelper)

        val powerManagerField = BeatDataSource::class.java.getDeclaredField("powerManager")
        powerManagerField.isAccessible = true
        powerManagerField.set(beatDataSource, powerManager)

        val contextField = BeatDataSource::class.java.getDeclaredField("context")
        contextField.isAccessible = true
        contextField.set(beatDataSource, context)

        val sensorFactoryField = BeatDataSource::class.java.getDeclaredField("sensorFactory")
        sensorFactoryField.isAccessible = true
        sensorFactoryField.set(beatDataSource, sensorFactory)

        every { context.getSystemService(Context.POWER_SERVICE) } returns powerManager
        every { context.packageName } returns "com.androbeat.androbeatagent"
        every { context.opPackageName } returns "com.androbeat.androbeatagent"
        every { context.opPackageName } returns "com.androbeat.androbeatagent"


        val contextManagerHandlerField = powerManager::class.java.getDeclaredField("mContext")
        contextManagerHandlerField.isAccessible = true
        contextManagerHandlerField.set(powerManager, context)

        every { powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Androbeat::BeatService-WakeLock") } returns wakeLock
 /*      every { wakeLock.acquire() } just runs
        every { wakeLock.release() } just runs*/

        val powerManagerHandlerField = powerManager::class.java.getDeclaredField("mHandler")
        powerManagerHandlerField.isAccessible = true
        powerManagerHandlerField.set(powerManager, mHandler)

        val mockIntent = mockk<Intent>(relaxed = true)
        every { logReader.readLogs() } just Runs

        // Ensure the PowerManager context is set to the mocked context
        val testContextField = powerManager::class.java.getDeclaredField("mContext")
        testContextField.isAccessible = true
        testContextField.set(powerManager, context)

        // Mock the ServiceUtils.setWakeLock method
        mockkObject(ServiceUtils)
        every { ServiceUtils.setWakeLock(powerManager) } returns wakeLock

        // Mock the newWakeLock and acquire methods
        every { powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Androbeat::BeatService-WakeLock") } returns wakeLock
        every { wakeLock.acquire(any()) } just Runs

        // Mock the createSensors method to add mock sensors
        every { sensorFactory.createSensor(context, 1) } returns mockSensor

        val sensorsField = BeatDataSource::class.java.getDeclaredField("_sensors")
        sensorsField.isAccessible = true
        sensorsField.set(beatDataSource, listOf(mockSensor))
    }

//TODO: FIX TESTS

//    @Test
//    fun testOnStartCommand() {
//        val intent = mockk<Intent>(relaxed = true)
//        beatService.onStartCommand(intent, 0, 1)
//        verify { beatDataSource.startBeat(intent) }
//    }
//
//    @Test
//    fun testOnDestroy() {
//        beatService.onDestroy()
//        verify { beatDataSource.destroy() }
//    }

}
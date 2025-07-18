// BeatServiceTest.kt
package com.androbeat.androbeatagent

import android.app.Notification
import com.androbeat.androbeatagent.data.local.BeatService
import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
class BeatServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var beatDataSource: BeatDataSource

    @Inject
    lateinit var serviceManager: ServiceManager

    private lateinit var beatService: BeatService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hiltRule.inject()

        beatService = spyk(BeatService(), recordPrivateCalls = true)
        beatService.beatDataSource = beatDataSource
        beatService.serviceManager = serviceManager

        every { serviceManager.setService(ofType(BeatService::class)) } just Runs
        every { serviceManager.setupService() } just Runs
        every { serviceManager.createServiceNotification() } returns mockk<Notification>(relaxed = true)
        every { beatDataSource.startBeat(any()) } just Runs
        every { beatDataSource.destroy() } just Runs
    }

    //    TODO: Fix this tests
    //    @Test
    //    fun testOnCreate() {
    //        beatService.onCreate()
    //        verify { serviceManager.setService(beatService) }
    //    }

    //
    //    @Test
    //    fun testOnStartCommand() {
    //        val intent = mockk<Intent>(relaxed = true)
    //        val result = beatService.onStartCommand(intent, 0, 1)
    //        verify { serviceManager.setupService() }
    //        verify { beatDataSource.startBeat(intent) }
    //        assert(result == Service.START_STICKY)
    //    }
    //
    //    @Test
    //    fun testOnDestroy() {
    //        beatService.onDestroy()
    //        verify { beatDataSource.destroy() }
    //    }

}
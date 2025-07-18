package com.androbeat.androbeatagent

import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.di.hiltModules.PermissionsModule
import com.androbeat.androbeatagent.domain.permissions.PermissionsManager
import com.androbeat.androbeatagent.presentation.view.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@UninstallModules(PermissionsModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockApi: RestApiInterface
    private lateinit var mockCall: Call<User>

    @Inject
    lateinit var mockPermissionsManager: PermissionsManager


    @Before
    fun setup() {

        hiltRule.inject()

        mockApi = mockk()
        mockCall = mockk()

        clearMocks(mockApi,  mockCall, mockPermissionsManager)
    }

    private fun launchActivityAndRequestPermissions(): ActivityScenario<MainActivity> {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val permArray = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.POST_NOTIFICATIONS
            )
            val permissionsNeeded: MutableList<String> = ArrayList()
            permArray.forEach { permission ->
                permissionsNeeded.add(permission)
            }
            ActivityCompat.requestPermissions(activity, permissionsNeeded.toTypedArray(), 123)
        }
        return scenario
    }

    @Test
    fun testMainActivityLaunches() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            Assert.assertNotNull(activity)
        }
        scenario.close()
    }

    private fun setupMocks(activity: MainActivity) {
        every { mockPermissionsManager.hasUsageAccessPermission() } returns true
    }

    private fun setupApiMock(latch: CountDownLatch) {
        coEvery { mockCall.enqueue(any()) } answers {
            firstArg<retrofit2.Callback<User>>().onResponse(
                mockCall,
                Response.success(User("test", "test"))
            )
            latch.countDown()
        }
    }

    private fun performButtonClick() {
        onView(withId(R.id.starStopButton)).perform(click())
    }

}
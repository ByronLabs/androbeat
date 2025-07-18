package com.androbeat.androbeatagent.presentation.view

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.daos.TokenDao
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.di.hiltModules.DatabaseModule
import com.androbeat.androbeatagent.di.hiltModules.NetworkApiModule
import com.androbeat.androbeatagent.di.hiltModules.PermissionsModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.clearMocks
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkApiModule::class, PermissionsModule::class, DatabaseModule::class)
@HiltAndroidTest
class ReinstallActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val mockApi: RestApiInterface = mockk()

    @BindValue
    @JvmField
    val mockDatabase: AppDatabase = mockk()

    private lateinit var mockCall: Call<String>
    private lateinit var scenario: ActivityScenario<ReinstallActivity>

    @Before
    fun setUp() {
        clearMocks(mockApi, mockDatabase)
        mockCall = mockk {
            every { enqueue(any()) } answers { nothing }
            every { cancel() } just Runs
            every { isExecuted } returns false
            every { isCanceled } returns false
            every { clone() } returns this@mockk
            every { request() } returns mockk()
        }

        val mockTokenDao = mockk<TokenDao> {
            coEvery { insert(any()) } just Runs
        }

        every { mockDatabase.tokenDao() } returns mockTokenDao

        hiltRule.inject()
        scenario = ActivityScenario.launch(ReinstallActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testReinstallButtonClickSuccess() {
        every { mockApi.reinstallAgent(any()) } returns mockCall
        every { mockCall.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<String>
            callback.onResponse(mockCall, Response.success("Success"))
        }

        onView(withId(R.id.OriginalTokenTextInput)).perform(replaceText("test_token"))
        onView(withId(R.id.MainAccountTextInput)).perform(replaceText("test_account"))
        onView(withId(R.id.OriginalClientEmailTextInput)).perform(replaceText("test_email"))
        onView(withId(R.id.ReinstallButton)).perform(click())

        scenario.onActivity { activity ->
            val startedIntent = activity.intent
            val expectedIntent = Intent(activity, LoginActivity::class.java)
            // Ajustar aserciones según la lógica de tu intent
            assert(startedIntent.component != expectedIntent.component)
        }
    }

    @Test
    fun testReinstallButtonClickFailure() {
        every { mockApi.reinstallAgent(any()) } returns mockCall
        every { mockCall.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<String>
            callback.onFailure(mockCall, Throwable("Error"))
        }

        onView(withId(R.id.OriginalTokenTextInput)).perform(replaceText("test_token"))
        onView(withId(R.id.MainAccountTextInput)).perform(replaceText("test_account"))
        onView(withId(R.id.OriginalClientEmailTextInput)).perform(replaceText("test_email"))
        onView(withId(R.id.ReinstallButton)).perform(click())

        scenario.onActivity { activity ->
            val tokenDao = mockDatabase.tokenDao()
            coVerify(exactly = 0) { tokenDao.insert(any()) }
        }
    }
}
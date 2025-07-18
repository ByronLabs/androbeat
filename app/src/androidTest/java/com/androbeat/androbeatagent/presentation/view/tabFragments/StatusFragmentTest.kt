package com.androbeat.androbeatagent.presentation.view.tabFragments

import android.widget.Button
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.presentation.tabFragments.StatusFragment
import com.androbeat.androbeatagent.presentation.view.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class StatusFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun testFragmentInjection() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.onActivity { activity ->
            val fragment = StatusFragment()
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

            assert(fragment.view != null)
        }
    }

    @Test
    fun testOnStart() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.onActivity { activity ->
            val fragment = StatusFragment()
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

            activity.supportFragmentManager.executePendingTransactions()

            val starStopButtonText = fragment.view?.findViewById<Button>(R.id.starStopButton)?.text
            val stateTextViewText = fragment.view?.findViewById<TextView>(R.id.stateTextView)?.text

            assert(starStopButtonText == fragment.getString(R.string.stop) ||
                    starStopButtonText == fragment.getString(R.string.start))
            assert(stateTextViewText == fragment.getString(R.string.service_running) ||
                    stateTextViewText == fragment.getString(R.string.service_not_running))
        }
    }

    @Test
    fun testOnResume() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.onActivity { activity ->
            val fragment = StatusFragment()
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

            activity.supportFragmentManager.executePendingTransactions()

            val starStopButtonText = fragment.view?.findViewById<Button>(R.id.starStopButton)?.text
            val stateTextViewText = fragment.view?.findViewById<TextView>(R.id.stateTextView)?.text

            assert(starStopButtonText == fragment.getString(R.string.stop) ||
                    starStopButtonText == fragment.getString(R.string.start))
            assert(stateTextViewText == fragment.getString(R.string.service_running) ||
                    stateTextViewText == fragment.getString(R.string.service_not_running))
        }
    }

    @Test
    fun testFetchAndSetClientId() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.onActivity { activity ->
            val fragment = StatusFragment()
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()

            activity.supportFragmentManager.executePendingTransactions()

            val clientIdTextView = fragment.view?.findViewById<TextView>(R.id.clientIdText)
            assert(clientIdTextView?.text != null)
        }
    }
    

}
package com.androbeat.androbeatagent.presentation.view

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReinstallActivityTest {

    @Test
    fun testReinstallActivityLaunches() {
        val scenario = ActivityScenario.launch(ReinstallActivity::class.java)
        scenario.onActivity { activity ->
            assertNotNull(activity)
        }
        scenario.close()
    }
}

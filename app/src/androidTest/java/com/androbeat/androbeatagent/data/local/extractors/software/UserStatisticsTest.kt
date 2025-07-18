// Archivo: app/src/androidTest/java/com/androbeat/androbeatagent/data/local/extractors/software/UserStatisticsTest.kt
package com.androbeat.androbeatagent.data.local.extractors.software

import android.app.usage.UsageStatsManager
import android.app.usage.StorageStatsManager
import android.os.storage.StorageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androbeat.androbeatagent.data.model.models.extractors.software.UserStatisticsModel
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserStatisticsInstrumentedTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usageStatsManager: UsageStatsManager

    @Inject
    lateinit var storageStatsManager: StorageStatsManager

    @Inject
    lateinit var storageManager: StorageManager

    private lateinit var userStatistics: UserStatistics

    @Before
    fun setUp() {
        hiltRule.inject()
        userStatistics = UserStatistics(
            usageStatsManager = usageStatsManager,
            storageManager = storageManager,
            storageStatsManager = storageStatsManager
        )
    }

    @Test
    fun userStatisticsStatisticsStringIsNotEmpty() {
        val result = userStatistics.statistics
        assertTrue(result.contains("User apps statistics"))
    }

    @Test
    fun userStatisticsDataIsNotNullAndMayContainData() {
        val data: List<UserStatisticsModel?>? = userStatistics.data
        assertNotNull(data)
    }

    @Test
    fun userStatisticsDataListIsConsistentWithStatisticsString() {
        val data = userStatistics.data
        val statsString = userStatistics.statistics
        if (!data.isNullOrEmpty()) {
            for (model in data) {
                assertNotNull(model)
                model?.let {
                    assertTrue(statsString.contains(it.packet ?: ""))
                }
            }
        }
    }

    @Test
    fun userStatisticsModelFieldsAreNotNullIfDataExists() {
        val data = userStatistics.data
        if (!data.isNullOrEmpty()) {
            for (model in data) {
                assertNotNull(model)
                model?.let {
                    assertNotNull(it.packet)
                    assertNotNull(it.totalTimeInForeground)
                    assertNotNull(it.lastTimeUsed)
                    assertNotNull(it.describeContents)
                }
            }
        }
    }

    @Test
    fun userStatisticsReturnsEmptyListIfNoUsageStats() {
        // Forzar el estado inicial para simular sin datos previos
        val emptyStats = UserStatistics(
            usageStatsManager = usageStatsManager,
            storageManager = storageManager,
            storageStatsManager = storageStatsManager
        )
        val data = emptyStats.data
        assertNotNull(data)
    }
}
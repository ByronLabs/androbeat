package com.androbeat.androbeatagent.extractors.hardware

import android.app.ActivityManager
import android.content.Context
import com.androbeat.androbeatagent.data.local.extractors.hardware.RamStatistics
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class RamStatisticsTest {
    private lateinit var ramStatistics: RamStatistics
    private val context = mockk<Context>()
    private val activityManager = mockk<ActivityManager>()


    @Before
    fun setup() {

        every { context.getSystemService(Context.ACTIVITY_SERVICE) } returns activityManager
        ramStatistics = RamStatistics(context)
    }

    @Test
    fun testGetRamStatistics() {
        val memoryInfo = ActivityManager.MemoryInfo().apply {
            availMem = 1024 * 1024 * 2 // 2 MB
            totalMem = 1024 * 1024 * 4 // 4 MB
        }

        every { activityManager.getMemoryInfo(any()) } answers {
            val memoryInfoArg = firstArg<ActivityManager.MemoryInfo>()
            memoryInfoArg.availMem = memoryInfo.availMem
            memoryInfoArg.totalMem = memoryInfo.totalMem
        }

        val expected = "Ram usage {00002 00004 00002}"
        val actual = ramStatistics.getRamStatistics()

        assertTrue(expected == actual)
    }
}
package com.androbeat.androbeatagent.data.local.extractors.hardware

import com.androbeat.androbeatagent.data.enums.DataProviderType
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Paths

class CpuStatisticsTest {

    private lateinit var cpuStatistics: CpuStatistics

    @Before
    fun setup() {
        cpuStatistics = CpuStatistics()
        mockkStatic(Files::class)
        mockkStatic(Paths::class)
        mockkStatic(RandomAccessFile::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testDataProviderProperty() {
        assertEquals(cpuStatistics, cpuStatistics.dataProvider)
    }

    @Test
    fun testTypeProperty() {
        assertEquals(DataProviderType.CPU_EXTRACTOR, cpuStatistics.type)
    }

}
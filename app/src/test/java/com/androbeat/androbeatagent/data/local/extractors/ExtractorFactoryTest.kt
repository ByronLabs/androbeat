package com.androbeat.androbeatagent.data.local.extractors

import android.content.Context
import com.androbeat.androbeatagent.data.local.extractors.hardware.CpuStatistics
import com.androbeat.androbeatagent.data.local.extractors.network.NetworkStatistics
import com.androbeat.androbeatagent.data.local.extractors.software.AppStatistics
import com.androbeat.androbeatagent.data.enums.ExtractorType
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtractorFactoryTest {

    private val context: Context = mockk()

    @Test
    fun createExtractor_should_return_NetworkStatistics_for_NETWORK_type() {
        val extractor = ExtractorFactory.createExtractor(context, ExtractorType.NETWORK)
        assertTrue(extractor is NetworkStatistics)
    }

    @Test
    fun createExtractor_should_return_CpuStatistics_for_CPU_type() {
        val extractor = ExtractorFactory.createExtractor(context, ExtractorType.CPU)
        assertTrue(extractor is CpuStatistics)
    }

    @Test
    fun createExtractor_should_return_AppStatistics_for_APP_type() {
        val extractor = ExtractorFactory.createExtractor(context, ExtractorType.APP)
        assertTrue(extractor is AppStatistics)
    }

    @Test
    fun createExtractor_should_throw_IllegalArgumentException_for_unsupported_type() {
        try {
            ExtractorFactory.createExtractor(context, null)
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Unsupported extractor type"))
        }
    }
}
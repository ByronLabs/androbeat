package com.androbeat.androbeatagent.data.repository.managers

import android.content.Context
import com.androbeat.androbeatagent.data.local.extractors.ExtractorFactory
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.definitions.ExtractorsDefinitions
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ExtractorManagerTest {

    private val context: Context = mockk()
    private val extractorFactory: ExtractorFactory = mockk()
    private val extractorManager = ExtractorManager(context, extractorFactory)

    @Before
    fun setUp() {
        every { extractorFactory.createExtractor(context, any()) } returns mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun createExtractorsShouldAddExtractorsToTheList() {
        extractorManager.createExtractors()
        assertEquals(ExtractorsDefinitions.extractorTypes.size, extractorManager.extractors.size)
    }

    @Test
    fun testGetProviders() {
        val dataProvider1: DataProvider<*> = mockk()
        val dataProvider2: DataProvider<*> = mockk()
        val dataExtractor1: DataExtractor = mockk {
            every { dataProvider } returns dataProvider1
        }
        val dataExtractor2: DataExtractor = mockk {
            every { dataProvider } returns dataProvider2
        }

        every { extractorFactory.createExtractor(context, any()) } returnsMany listOf(dataExtractor1, dataExtractor2)

        extractorManager.createExtractors()
        val providers = extractorManager.getProviders()

        assertEquals(12, providers.size)
        assertEquals(dataProvider1, providers[0])
        assertEquals(dataProvider2, providers[1])
    }
}
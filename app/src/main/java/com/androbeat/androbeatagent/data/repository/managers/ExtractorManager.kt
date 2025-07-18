package com.androbeat.androbeatagent.data.repository.managers

import android.content.Context
import com.androbeat.androbeatagent.data.local.extractors.ExtractorFactory
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.data.IExtractorManager
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.definitions.ExtractorsDefinitions
import javax.inject.Inject

class ExtractorManager @Inject constructor(
    private val context: Context,
    private val extractorFactory: ExtractorFactory
) : IExtractorManager {
    private val _extractors = mutableListOf<DataExtractor>()
    private val extractorTypes = ExtractorsDefinitions.extractorTypes

    val extractors: List<DataExtractor>
        get() = _extractors

    override fun createExtractors() {
        for (extractorType in extractorTypes) {
            extractorFactory.createExtractor(context, extractorType).let {
                _extractors.add(it)
            }
        }
    }

    override fun getProviders(): List<DataProvider<*>> {
        return extractors.map { it.dataProvider }
    }

    override fun getExtractorNames(): List<String> {
        return extractors.map { it.javaClass.simpleName }
    }
}
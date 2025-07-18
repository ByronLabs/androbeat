package com.androbeat.androbeatagent.domain.extractors

import com.androbeat.androbeatagent.domain.data.DataProvider

interface DataExtractor {
    val statistics: String?
    val dataProvider: DataProvider<*>
}
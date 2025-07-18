package com.androbeat.androbeatagent.domain.data

import com.androbeat.androbeatagent.data.enums.DataProviderType

interface DataProvider<T> {
    val type: DataProviderType?
    val data: T
}
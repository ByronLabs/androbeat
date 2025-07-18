package com.androbeat.androbeatagent.data.definitions

import com.androbeat.androbeatagent.data.enums.ExtractorType

object ExtractorsDefinitions {

    val extractorTypes = arrayOf(
        ExtractorType.BATTERY,
        ExtractorType.CPU,
        ExtractorType.NETWORK,
        ExtractorType.APP,
        ExtractorType.APPSTATS,
        ExtractorType.WIFI,
        ExtractorType.WIFI_LIST,
        ExtractorType.BASIC_CONFIGURATION,
        ExtractorType.CELL_TOWER,
        ExtractorType.BT_LIST,
        ExtractorType.ENV_VARIABLE_LIST,
        ExtractorType.RAM_EXTRACTOR
    )
}
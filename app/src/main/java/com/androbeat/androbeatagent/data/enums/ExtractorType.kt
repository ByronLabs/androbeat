package com.androbeat.androbeatagent.data.enums

enum class ExtractorType(val value: Int) {
    NETWORK(0), CPU(1), APP(2), BATTERY(3), APPSTATS(4), WIFI(5), WIFI_LIST(6), BASIC_CONFIGURATION(
        7
    ),
    CELL_TOWER(8), BT_LIST(9), ENV_VARIABLE_LIST(10), RAM_EXTRACTOR(11)

}
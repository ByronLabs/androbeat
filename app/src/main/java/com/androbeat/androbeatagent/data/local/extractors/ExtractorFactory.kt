package com.androbeat.androbeatagent.data.local.extractors

import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.storage.StorageManager
import com.androbeat.androbeatagent.data.local.extractors.hardware.BatteryStatistics
import com.androbeat.androbeatagent.data.local.extractors.hardware.BtList
import com.androbeat.androbeatagent.data.local.extractors.hardware.CpuStatistics
import com.androbeat.androbeatagent.data.local.extractors.hardware.RamStatistics
import com.androbeat.androbeatagent.data.local.extractors.network.CellTower
import com.androbeat.androbeatagent.data.local.extractors.network.NetworkStatistics
import com.androbeat.androbeatagent.data.local.extractors.network.WifiConnectedInfo
import com.androbeat.androbeatagent.data.local.extractors.network.WifiList
import com.androbeat.androbeatagent.data.local.extractors.software.AppStatistics
import com.androbeat.androbeatagent.data.local.extractors.software.BasicConfiguration
import com.androbeat.androbeatagent.data.local.extractors.software.EnvironmentVariables
import com.androbeat.androbeatagent.data.local.extractors.software.UserStatistics
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.ExtractorType

object ExtractorFactory {
    sealed class ExtractorCreator {
        data class WithContext(val creator: (Context) -> DataExtractor) : ExtractorCreator()
        data class WithoutContext(val creator: () -> DataExtractor) : ExtractorCreator()
    }

    private val extractorCreators: Map<ExtractorType, ExtractorCreator> = mapOf(
        ExtractorType.NETWORK to ExtractorCreator.WithContext { context -> NetworkStatistics(context) },
        ExtractorType.CPU to ExtractorCreator.WithoutContext { CpuStatistics() },
        ExtractorType.APP to ExtractorCreator.WithContext { context -> AppStatistics(context) },
        ExtractorType.BATTERY to ExtractorCreator.WithContext { context -> BatteryStatistics(context) },
        ExtractorType.APPSTATS to ExtractorCreator.WithContext { context ->
            UserStatistics(
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager,
                context.getSystemService(Context.STORAGE_SERVICE) as StorageManager,
                context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            )
        },
        ExtractorType.WIFI to ExtractorCreator.WithContext { context -> WifiConnectedInfo(context) },
        ExtractorType.WIFI_LIST to ExtractorCreator.WithContext { context -> WifiList(context) },
        ExtractorType.BASIC_CONFIGURATION to ExtractorCreator.WithContext { context -> BasicConfiguration(context) },
        ExtractorType.CELL_TOWER to ExtractorCreator.WithContext { context -> CellTower(context) },
        ExtractorType.BT_LIST to ExtractorCreator.WithContext { context -> BtList(context) },
        ExtractorType.ENV_VARIABLE_LIST to ExtractorCreator.WithoutContext { EnvironmentVariables() },
        ExtractorType.RAM_EXTRACTOR to ExtractorCreator.WithContext { context -> RamStatistics(context) }
    )

    fun createExtractor(context: Context, type: ExtractorType?): DataExtractor {
        val creator = extractorCreators[type]
            ?: throw IllegalArgumentException("Unsupported extractor type: $type")
        return when (creator) {
            is ExtractorCreator.WithContext -> creator.creator(context)
            is ExtractorCreator.WithoutContext -> creator.creator()
        }
    }
}
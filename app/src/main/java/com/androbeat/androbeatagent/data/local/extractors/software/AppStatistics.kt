package com.androbeat.androbeatagent.data.local.extractors.software

import android.content.Context
import android.content.pm.PackageManager

import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger

class AppStatistics(private val context: Context) : DataExtractor, DataProvider<List<String?>?> {



    private val _stats = mutableListOf<String>()
    private val stats: List<String>
        get() = _stats

    fun getAppStatistics(): String {
        try {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            _stats.clear()
            _stats.addAll(packages.map { it.packageName })

            val appsString = packages.joinToString(separator = " ", prefix = "Apps{", postfix = "}") { it.packageName }

            if (BuildConfig.DEBUG_EXTRACTORS) {
                Logger.logInfo(TAG, appsString)
            }
            return appsString
        } catch (throwable: Throwable) {
            Logger.logError(TAG, "Error getting app statistics: ${throwable.localizedMessage}  $throwable")
            return ""
        }
    }
    
    override val statistics: String
        get() = this.getAppStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.APP_EXTRACTOR
    override val data: List<String?>
        get() = stats

    companion object {
        private val TAG = AppStatistics::class.java.simpleName
    }
}
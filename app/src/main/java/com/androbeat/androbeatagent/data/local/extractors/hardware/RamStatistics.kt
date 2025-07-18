package com.androbeat.androbeatagent.data.local.extractors.hardware

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.RamModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType

class RamStatistics(val context: Context) : DataExtractor, DataProvider<RamModel?> {
    private var ramModel: RamModel? =  RamModel(0, 0, 0)
     @SuppressLint("DefaultLocale")
     fun getRamStatistics(): String {
         val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
         val memoryInfo = ActivityManager.MemoryInfo()
         activityManager.getMemoryInfo(memoryInfo)

         val availMem = memoryInfo.availMem / (1024 * 1024) // in MB
         val totalMem = memoryInfo.totalMem / (1024 * 1024) // in MB
         val usedMem = totalMem - availMem // in MB

         ramModel = RamModel(availMem, totalMem, usedMem)

         val ramData = String.format(
             "Ram usage {%05d %05d %05d}",
             ramModel!!.availableMemory,
             ramModel!!.totalMemory,
             ramModel!!.usedMemory
         )
        return ramData
    }

    override val statistics: String
        get() = this.getRamStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.RAM_EXTRACTOR
    override val data: RamModel?
        get() = ramModel!!

    companion object {
        private val TAG = RamStatistics::class.java.simpleName
    }

}
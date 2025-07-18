package com.androbeat.androbeatagent.data.local.extractors.hardware

import android.os.Build
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.enums.DataProviderType
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.CpuModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.utils.ApplicationStatus
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class CpuStatistics : DataExtractor, DataProvider<List<CpuModel?>?> {


    private val _stats = mutableListOf<CpuModel>()
    private val stats: List<CpuModel>
        get() = _stats
    private val numberOfCores: Int
        get() {
            if (Build.VERSION.SDK_INT >= 17) {
                return Runtime.getRuntime().availableProcessors()
            }
            var cpuAvailable: Long
            try {
                Files.list(Paths.get(CPU_INFO_DIR)).use { files ->
                    cpuAvailable = files.filter { file: Path ->
                        file.fileName.toString().matches("cpu[0-9]".toRegex())
                    }
                        .count()
                }
            } catch (e: IOException) {
                ApplicationStatus.postError("Error obtaining CPU statistics: ${e.message}")
                return 0
            }
            ApplicationStatus.postOK()
            return cpuAvailable.toInt()
        }

    private fun getMinMaxFreq(core: Int): LongArray {
        val freqs = longArrayOf(-1, -1)
        try {
            val minFreqFile = RandomAccessFile(
                CPU_INFO_DIR + "cpu" + core
                        + "/cpufreq/cpuinfo_min_freq", "r"
            )
            val maxFreqFile = RandomAccessFile(
                CPU_INFO_DIR + "cpu" + core
                        + "/cpufreq/cpuinfo_max_freq", "r"
            )
            freqs[0] = minFreqFile.readLine().toLong() / 1000
            freqs[1] = maxFreqFile.readLine().toLong() / 1000
            minFreqFile.close()
            maxFreqFile.close()
        } catch (e: IOException) {
            Pair(0,0)
        }
        return freqs
    }

    private fun getCurrentFreq(core: Int): Long {
        val freq: Long
        try {
            val curFreqFile = RandomAccessFile(
                CPU_INFO_DIR + "cpu" + core
                        + "/cpufreq/scaling_cur_freq", "r"
            )
            freq = curFreqFile.readLine().toLong() / 1000
            curFreqFile.close()
        } catch (e: IOException) {
            return 0L
        }
        return freq
    }

    private val cpuStats: List<CpuModel>
        // + Info:
        get() {
            val cores = numberOfCores
            _stats.clear()
            for (core in 0 until cores) {
                val minMax = getMinMaxFreq(core)
                val current = getCurrentFreq(core)
                _stats.add(CpuModel(longArrayOf(minMax[0], minMax[1], current)))
            }
            return stats
        }

    private fun getCpuStatistics(): String {
        cpuStats
        val cpuData = StringBuilder("CPU{")
        for (coreStats in stats) {
            cpuData.append("[")
                .append(coreStats.minFreq).append(" ")
                .append(coreStats.maxFreq).append(" ")
                .append(coreStats.curFreq)
                .append("]")
        }
        cpuData.append("}")
        if (BuildConfig.DEBUG_EXTRACTORS) {
            Logger.logInfo(TAG, cpuData.toString())
        }
        return cpuData.toString()
    }

    override val statistics: String
        get() = this.getCpuStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.CPU_EXTRACTOR
    override val data: List<CpuModel?>
        get() = stats

    companion object {
        private val TAG = CpuStatistics::class.java.simpleName
        private const val CPU_INFO_DIR = "/sys/devices/system/cpu/"
    }


}
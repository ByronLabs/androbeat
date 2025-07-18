package com.androbeat.androbeatagent.data.local.logReader

import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.domain.data.LogReader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LogReaderImp : LogReader {


    override fun readLogs() {
        val thread = Thread {
            try {
                val p = Runtime.getRuntime().exec("logcat -d")
                val bufferedReader = BufferedReader(InputStreamReader(p.inputStream))
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    Logger.logDebug("LOG", line!!)
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        thread.start()
    }
}





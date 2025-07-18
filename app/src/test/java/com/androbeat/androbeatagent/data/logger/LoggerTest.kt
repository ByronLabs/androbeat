package com.androbeat.androbeatagent.data.logger

import android.util.Log
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

class LoggerTest {

    init {
        mockkStatic(Log::class)
    }

    @Test
    fun logsInfoMessage() {
        val tag = "INFO_TAG"
        val message = "Info message"
        Logger.logInfo(tag, message)
        verify { Log.i(tag, message) }
    }

    @Test
    fun logsDebugMessage() {
        val tag = "DEBUG_TAG"
        val message = "Debug message"
        Logger.logDebug(tag, message)
        verify { Log.d(tag, message) }
    }

    @Test
    fun logsErrorMessage() {
        val tag = "ERROR_TAG"
        val message = "Error message"
        Logger.logError(tag, message)
        verify { Log.e(tag, message) }
    }

    @Test
    fun logsWarningMessage() {
        val tag = "WARNING_TAG"
        val message = "Warning message"
        Logger.logWarning(tag, message)
        verify { Log.w(tag, message) }
    }

    @Test
    fun logsVerboseMessage() {
        val tag = "VERBOSE_TAG"
        val message = "Verbose message"
        Logger.logVerbose(tag, message)
        verify { Log.v(tag, message) }
    }
}
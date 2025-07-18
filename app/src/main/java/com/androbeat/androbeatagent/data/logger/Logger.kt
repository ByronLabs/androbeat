package com.androbeat.androbeatagent.data.logger

import android.util.Log

object Logger {

    fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun logDebug(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun logWarning(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun logVerbose(tag: String, message: String) {
        Log.v(tag, message)
    }
}
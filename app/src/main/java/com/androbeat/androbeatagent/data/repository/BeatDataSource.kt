package com.androbeat.androbeatagent.data.repository

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import com.androbeat.androbeatagent.data.repository.managers.DataProcessingManager
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import com.androbeat.androbeatagent.domain.data.LogReader
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Named

class BeatDataSource @Inject constructor(
    private val serviceManager: ServiceManager,
    private val dataProcessingManager: DataProcessingManager,
    context: Context,
    private val networkHandler: NetworkHandler,
    @Named("ReconnectExecutor") private val reconnectExecutor: ExecutorService
) {
    companion object {
        private val TAG = BeatDataSource::class.java.simpleName
    }

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

            if (isConnected) {
                reconnectExecutor.execute {
                    networkHandler.processPendingData()
                }
            }
        }
    }

    fun startBeat(intent: Intent?) {
        registerNetworkReceiver()
        serviceManager.setupService()
        dataProcessingManager.createSensors()
        dataProcessingManager.createExtractors()
        dataProcessingManager.registerProviders()
        dataProcessingManager.startDataProcessingCoroutine()
        Logger.logInfo(TAG, "Started service...")
    }

    private fun registerNetworkReceiver() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun destroy() {
        dataProcessingManager.unregisterSensors()
        dataProcessingManager.stopDataProcessingCoroutine()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        Logger.logInfo(TAG, "Service destroyed...")
    }
}
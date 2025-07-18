package com.androbeat.androbeatagent.data.repository

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.androbeat.androbeatagent.data.repository.BeatDataSource
import com.androbeat.androbeatagent.data.repository.NetworkHandler
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import com.androbeat.androbeatagent.data.repository.managers.DataProcessingManager
import com.androbeat.androbeatagent.data.repository.managers.ServiceManager
import com.androbeat.androbeatagent.domain.data.LogReader
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class BeatDataSourceTest {

    private lateinit var beatDataSource: BeatDataSource
    private lateinit var dataManager: DataManagerImp
    private lateinit var logReader: LogReader
    private lateinit var serviceManager: ServiceManager
    private lateinit var dataProcessingManager: DataProcessingManager
    private lateinit var context: Context
    private lateinit var networkHandler: NetworkHandler
    private lateinit var reconnectExecutor: ExecutorService
    private lateinit var connectivityManager: ConnectivityManager

    @Before
    fun setUp() {
        serviceManager = mockk(relaxed = true)
        dataProcessingManager = mockk(relaxed = true)
        context = mockk(relaxed = true)
        networkHandler = mockk(relaxed = true)
        reconnectExecutor = mockk(relaxed = true)
        connectivityManager = mockk(relaxed = true)
        val networkRequestBuilder = mockk<NetworkRequest.Builder>(relaxed = true)
        mockkConstructor(NetworkRequest.Builder::class)
        every { anyConstructed<NetworkRequest.Builder>().addCapability(any()) } returns networkRequestBuilder
        every { networkRequestBuilder.build() } returns mockk(relaxed = true)
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        beatDataSource = BeatDataSource(serviceManager, dataProcessingManager, context, networkHandler, reconnectExecutor)
    }

    @Test
    fun testStartBeat() {
        val intent = mockk<Intent>(relaxed = true)
        every { intent.getStringExtra("CLIENT_ID") } returns "TEST_CLIENT_ID"

        beatDataSource.startBeat(intent)

        verify { serviceManager.setupService() }
        verify { dataProcessingManager.createSensors() }
        verify { dataProcessingManager.createExtractors() }
        verify { dataProcessingManager.registerProviders() }
        verify { dataProcessingManager.startDataProcessingCoroutine() }
        verify { connectivityManager.registerNetworkCallback(any(), any<ConnectivityManager.NetworkCallback>()) }
    }

    @Test
    fun testDestroy() {
        beatDataSource.destroy()

        verify { dataProcessingManager.unregisterSensors() }
        verify { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) }
    }

    @Test
    fun onAvailablewhenNetworkIsConnectedshouldExecuteNetworkHandlerProcessPendingData() {

        val connectivityManager = mockk<ConnectivityManager>()
        val networkHandler = mockk<NetworkHandler>(relaxed = true)
        val reconnectExecutor = mockk<Executor>()
        val network = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>()

        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true


        val slot = slot<Runnable>()
        every { reconnectExecutor.execute(capture(slot)) } answers {
            slot.captured.run()
        }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

                if (isConnected) {
                    reconnectExecutor.execute {
                        networkHandler.processPendingData()
                    }
                }
            }
        }

        networkCallback.onAvailable(network)

        verify { networkHandler.processPendingData() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
package com.androbeat.androbeatagent.data.local.extractors.hardware

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BtDeviceModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BtListTest {

    private lateinit var context: Context
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var btList: BtList

    @Before
    fun setup() {
        context = mockk()
        bluetoothAdapter = mockk()
        btList = BtList(context)

        mockkStatic(BluetoothAdapter::class)
        every { BluetoothAdapter.getDefaultAdapter() } returns bluetoothAdapter
    }

    @After
    fun tearDown() {
        unmockkStatic(BluetoothAdapter::class)
    }


    @Test
    fun testGenerateLogString() {
        val devices = listOf(
            BtDeviceModel().apply {
                name = "DeviceName"
                address = "00:11:22:33:44:55"
                alias = "DeviceAlias"
            }
        )

        val generateLogStringMethod = BtList::class.java.getDeclaredMethod("generateLogString", List::class.java)
        generateLogStringMethod.isAccessible = true
        val logString = generateLogStringMethod.invoke(btList, devices) as String

        val expectedLogString = "BT Device list {[DeviceName DeviceAlias 00:11:22:33:44:55]}"
        assertEquals(expectedLogString, logString)
    }

    @Test
    fun testGetStatistics() {
        val device: BluetoothDevice = mockk()
        val devices = setOf(device)
        every { device.name } returns "DeviceName"
        every { device.address } returns "00:11:22:33:44:55"
        every { bluetoothAdapter.bondedDevices } returns devices
        every { ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) } returns PackageManager.PERMISSION_GRANTED

        // Ensure the BluetoothAdapter is correctly mocked
        mockkStatic(BluetoothAdapter::class)
        every { BluetoothAdapter.getDefaultAdapter() } returns bluetoothAdapter

        // Reinitialize btList to use the mocked BluetoothAdapter
        btList = BtList(context)

        val expectedStatistics = "BT Device list {[DeviceName  00:11:22:33:44:55]}"
        assertEquals(expectedStatistics, btList.statistics)
    }
}
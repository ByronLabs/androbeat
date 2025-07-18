package com.androbeat.androbeatagent.data.local.extractors.hardware

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.androbeat.androbeatagent.data.model.models.extractors.hardware.BtDeviceModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType

class BtList(private val context: Context) : DataExtractor, DataProvider<List<BtDeviceModel?>?> {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val btList: List<BtDeviceModel>
        get() = fetchPairedDevices()

    private fun fetchPairedDevices(): List<BtDeviceModel> {
        val devices = mutableListOf<BtDeviceModel>()
        if (bluetoothAdapter != null && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.bondedDevices?.forEach { device ->
                devices.add(
                    BtDeviceModel().apply {
                        name = device.name
                        address = device.address
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            alias = device.alias
                        }
                    }
                )
            }
        }
        return devices
    }

    private fun generateLogString(devices: List<BtDeviceModel>): String =
        devices.joinToString(separator = ", ", prefix = "BT Device list {", postfix = "}") { device ->
            "[${device.name} ${device.alias ?: ""} ${device.address}]"
        }

    override val statistics: String
        get() = generateLogString(btList)
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.BT_LIST
    override val data: List<BtDeviceModel?>
        get() = btList
}
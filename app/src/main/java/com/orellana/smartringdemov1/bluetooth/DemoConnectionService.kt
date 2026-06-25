package com.orellana.smartringdemov1.bluetooth

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.orellana.smartringdemov1.MAC_ADDRESS_INTENT_EXTRA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DemoConnectionService: Service() {

    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter

    //state
    private val _state = MutableStateFlow(ServiceState())
    val state = _state.asStateFlow()


    override fun onCreate() {
        super.onCreate()
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
    }

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onBind(intent: Intent?): IBinder? {
        intent?.let  {
            val macAddress = it.getStringExtra(MAC_ADDRESS_INTENT_EXTRA)
            val device = bluetoothAdapter.getRemoteDevice(macAddress)
            device.connectGatt(this, true, DemoGattCallback())
            val demoBinder = DemoConnectionBinder()
            return demoBinder
        }
        return null
    }


    inner class DemoConnectionBinder: Binder() {

        fun getService(): DemoConnectionService {
            return this@DemoConnectionService
        }

    }
}


data class ServiceState(
    val isConnected: Boolean = false
)
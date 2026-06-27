package com.orellana.smartringdemov1.bluetooth

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.orellana.smartringdemov1.MAC_ADDRESS_INTENT_EXTRA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DemoConnectionService : Service() {

    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter

    //connection gatt
    private var gatt: BluetoothGatt? = null

    fun updateGatt(gatt: BluetoothGatt?) {
        this.gatt = gatt
    }

    //state
    private val _state = MutableStateFlow(ServiceState())
    val state = _state.asStateFlow()


    //state functions
    private fun updateIsConnected(connectionState: ServiceState.ConnectionState) {
        _state.update { current ->
            current.copy(connectionState = connectionState)
        }
    }


    //custom gatt callback
    val callback = DemoGattCallback(
        updateIsConnected = this::updateIsConnected,
        updateGatt = this::updateGatt
    )


    override fun onCreate() {
        super.onCreate()
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
    }

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onBind(intent: Intent?): IBinder? {
        intent?.let {
            val macAddress = it.getStringExtra(MAC_ADDRESS_INTENT_EXTRA)
            val device = bluetoothAdapter.getRemoteDevice(macAddress)
            device.connectGatt(
                this,
                true,
                callback
            )
            val demoBinder = DemoConnectionBinder()

            Log.d("CONNECT", "call onBind() on DecomConnectionService()")
            Log.d("CONNECT", "demoBinder: $demoBinder")
            return demoBinder
        }
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnectRing() {
        gatt?.disconnect()
    }


    inner class DemoConnectionBinder : Binder() {

        fun getService(): DemoConnectionService {
            return this@DemoConnectionService
        }

    }
}


data class ServiceState(
    val connectionState: ConnectionState = ConnectionState.CONNECTION_STATE_DISCONNECTED
) {
    enum class ConnectionState {
        CONNECTION_STATE_DISCONNECTED,
        CONNECTION_STATE_CONNECTING,
        CONNECTION_STATE_CONNECTED
    }
}
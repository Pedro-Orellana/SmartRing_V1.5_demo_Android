package com.orellana.smartringdemov1.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DemoGattCallback(
    val updateIsConnected: (ServiceState.ConnectionState) -> Unit,
    val updateGatt:(BluetoothGatt?) -> Unit
): BluetoothGattCallback() {

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            //successfully connected to the ring
            Log.d("CONNECT", "Ring is connected")
                updateGatt(gatt)
                updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED)
                gatt?.discoverServices()
        }

        if(newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d("CONNECT", "Ring is not connected")
            updateGatt(null)
           updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_DISCONNECTED)
        }

        if(newState == BluetoothProfile.STATE_CONNECTING) {
            Log.d("CONNECT", "Trying to connect to ring...")
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING)
        }
    }
}
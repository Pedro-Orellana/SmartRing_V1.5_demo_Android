package com.orellana.smartringdemov1.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile

class DemoGattCallback(
    val updateIsConnected: (ServiceState.ConnectionState) -> Unit,
    val updateGatt: (BluetoothGatt?) -> Unit,
    val startForeground: () -> Unit
) : BluetoothGattCallback() {

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            //successfully connected to the ring
            updateGatt(gatt)
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED)
            startForeground()
            gatt?.discoverServices()
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            updateGatt(null)
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_DISCONNECTED)
        }

        if (newState == BluetoothProfile.STATE_CONNECTING) {
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING)
        }
    }
}
package com.orellana.smartringdemov1.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DemoGattCallback(
   val updateIsConnected: (Boolean) -> Unit,
   val updateGatt:(BluetoothGatt?) -> Unit
): BluetoothGattCallback() {

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            //successfully connected to the ring
                updateGatt(gatt)
                updateIsConnected(true)
                gatt?.discoverServices()
        }

        if(newState == BluetoothProfile.STATE_DISCONNECTED) {
            updateGatt(null)
           updateIsConnected(false)
        }
    }
}
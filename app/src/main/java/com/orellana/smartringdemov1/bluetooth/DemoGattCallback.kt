package com.orellana.smartringdemov1.bluetooth

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.util.Log
import androidx.annotation.RequiresPermission
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

class DemoGattCallback(
    val updateIsConnected: (ServiceState.ConnectionState) -> Unit,
    val updateAndCancelJob: (BluetoothGatt?, BluetoothGattCharacteristic?) -> Unit,
    val updateSensorData: (Float, Float, Float, Float, Float, Float) -> Unit,
    val startForeground: () -> Unit
) : BluetoothGattCallback() {

    val nusServiceUUID: UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
    val rxCharacteristicUUID: UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")
    val txCharacteristicUUID: UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E")
    val cccDescriptorUUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    lateinit var nusService: BluetoothGattService
    lateinit var rxCharacteristic: BluetoothGattCharacteristic
    lateinit var txCharacteristic: BluetoothGattCharacteristic
    lateinit var cccDescriptor: BluetoothGattDescriptor

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            //successfully connected to the ring
            gatt?.discoverServices()
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            updateAndCancelJob(null, null)
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_DISCONNECTED)
        }

        if (newState == BluetoothProfile.STATE_CONNECTING) {
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        gatt?.let  {
            nusService = it.getService(nusServiceUUID)
            txCharacteristic = nusService.getCharacteristic(txCharacteristicUUID)
            rxCharacteristic = nusService.getCharacteristic(rxCharacteristicUUID)
            cccDescriptor = txCharacteristic.getDescriptor(cccDescriptorUUID)
            enableCharacteristicNotifications(it, txCharacteristic)
        }
    }


    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
        descriptor?.let {
            if(it.uuid.equals(cccDescriptorUUID) && (status == BluetoothGatt.GATT_SUCCESS)) {
                //notifications are enabled, update UI
                Log.d("DATA", "notifications are enabled")
                updateAndCancelJob(gatt, rxCharacteristic)
                updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED)
                startForeground()
            }
        }
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {
        super.onCharacteristicChanged(gatt, characteristic, value)
        //this is where data is obtained
        val buffer = ByteBuffer.wrap(value)
            .order(ByteOrder.LITTLE_ENDIAN)

        //get data
        val yaw = (buffer.getShort() / 16.0f)
        val roll = (buffer.getShort() / 16.0f)
        val pitch = (buffer.getShort() / 16.0f)
        val xAccel = (buffer.getShort() / 100.0f)
        val yAccel = (buffer.getShort() / 100.0f)
        val zAccel = (buffer.getShort() / 100.0f)

        updateSensorData(xAccel, yAccel, zAccel, yaw, pitch, roll)
        //Log.d("DATA", "heading: $yaw, roll: $roll, pitch: $pitch, X: $xAccel, Y: $yAccel, Z: $zAccel")
    }




    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun enableCharacteristicNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        gatt.setCharacteristicNotification(characteristic, true)
        gatt.writeDescriptor(cccDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
    }
}
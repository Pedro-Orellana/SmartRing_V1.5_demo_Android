package com.orellana.smartringdemov1.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.companion.AssociationInfo
import androidx.annotation.RequiresPermission

data class SmartRingDevice(
    val name: String,
    val address: String,
) {

    companion object {


        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        fun getSmartRingFromBluetoothDevice(device: BluetoothDevice) : SmartRingDevice {
            return SmartRingDevice(
                name = device.name,
                address = device.address
            )
        }


        fun getSmartRingFromAssociation(association: AssociationInfo): SmartRingDevice {
            return SmartRingDevice(
                name = association.displayName.toString(),
                address = association.deviceMacAddress.toString().uppercase()
            )
        }
    }
}

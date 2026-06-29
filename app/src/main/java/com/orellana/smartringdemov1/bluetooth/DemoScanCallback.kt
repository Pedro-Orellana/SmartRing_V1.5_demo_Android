package com.orellana.smartringdemov1.bluetooth

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.orellana.smartringdemov1.viewmodels.HomeState

class DemoScanCallback(
    val updateRingDevice: (SmartRingDevice) -> Unit,
    val updateScanState: (HomeState.ScanState) -> Unit,
    val cancelScanJob: () -> Unit
) : ScanCallback() {

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        result?.let {
            val ring = SmartRingDevice.getSmartRingFromBluetoothDevice(it.device)
            updateRingDevice(ring)
            updateScanState(HomeState.ScanState.SCAN_STATE_FOUND)
            cancelScanJob()
        }
    }

}
package com.orellana.smartringdemov1.viewmodels

import com.orellana.smartringdemov1.bluetooth.SmartRingDevice

data class HomeState(
    val newDevice: SmartRingDevice? = null,
    val isConnected: Boolean = false,
    val scanState: ScanState = ScanState.SCAN_STATE_IDLE
    ) {

    enum class ScanState {
        SCAN_STATE_IDLE,
        SCAN_STATE_SCANNING,
        SCAN_STATE_FOUND
    }
}

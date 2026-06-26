package com.orellana.smartringdemov1.viewmodels

import com.orellana.smartringdemov1.bluetooth.ServiceState
import com.orellana.smartringdemov1.bluetooth.SmartRingDevice

data class HomeState(
    val newDevice: SmartRingDevice? = null,
    val scanState: ScanState = ScanState.SCAN_STATE_IDLE,
    val serviceState: ServiceState = ServiceState()
    ) {

    enum class ScanState {
        SCAN_STATE_IDLE,
        SCAN_STATE_SCANNING,
        SCAN_STATE_FOUND
    }
}

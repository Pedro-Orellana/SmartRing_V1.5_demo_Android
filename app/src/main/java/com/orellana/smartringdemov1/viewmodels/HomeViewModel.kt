package com.orellana.smartringdemov1.viewmodels

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.companion.CompanionDeviceManager
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orellana.smartringdemov1.bluetooth.DemoScanCallback
import com.orellana.smartringdemov1.bluetooth.SmartRingDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(val app: Application): AndroidViewModel(application = app) {

    val companionDeviceManager: CompanionDeviceManager = app.getSystemService(CompanionDeviceManager::class.java)
    val bluetoothManager: BluetoothManager = app.getSystemService(BluetoothManager::class.java)
    val bluetoothScanner: BluetoothLeScanner = bluetoothManager.adapter.bluetoothLeScanner

    init {
        //check if ring device is already a companion device
        if(companionDeviceManager.myAssociations.isNotEmpty()) {
            val associatedDevice = companionDeviceManager.myAssociations[0]
            val ringDevice = SmartRingDevice(associatedDevice.displayName.toString(), associatedDevice.deviceMacAddress.toString())
            updateRingDevice(ringDevice)
            updateScanState(HomeState.ScanState.SCAN_STATE_FOUND)
        }
    }
    val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val callback = DemoScanCallback(this::updateRingDevice, this::updateScanState, this::stopScanWork)


    //state functions
    private fun updateRingDevice(ringDevice: SmartRingDevice) {
        _state.update { currentState ->
            currentState.copy(newDevice = ringDevice)
        }
    }

    private fun updateConnectionStatus(isConnected: Boolean) {
        _state.update { currentState ->
            currentState.copy(isConnected = isConnected)
        }
    }

    private fun updateScanState(scanState: HomeState.ScanState) {
        _state.update { currentState ->
            currentState.copy(scanState = scanState)
        }
    }

    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    fun startScanning() {
        viewModelScope.launch  {
            doScanWork()
            delay(5000.milliseconds)
            stopScanWork()
        }
    }


    private fun doScanWork() {
        updateScanState(HomeState.ScanState.SCAN_STATE_SCANNING)
        val filter = ScanFilter.Builder()
            .setDeviceName("Demo")
            .build()
        val filters = listOf(filter)

        val settings = ScanSettings.Builder()
            .build()

        if(app.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            bluetoothScanner.startScan(filters, settings, callback)
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun stopScanWork() {
        bluetoothScanner.stopScan(callback)
        updateScanState(HomeState.ScanState.SCAN_STATE_IDLE)
    }

}
package com.orellana.smartringdemov1.viewmodels

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothLeDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orellana.smartringdemov1.DemoApp
import com.orellana.smartringdemov1.bluetooth.DemoScanCallback
import com.orellana.smartringdemov1.bluetooth.ServiceState
import com.orellana.smartringdemov1.bluetooth.SmartRingDevice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(val app: Application): AndroidViewModel(application = app) {

    val companionDeviceManager: CompanionDeviceManager = app.getSystemService(CompanionDeviceManager::class.java)
    val bluetoothManager: BluetoothManager = app.getSystemService(BluetoothManager::class.java)
    val bluetoothScanner: BluetoothLeScanner = bluetoothManager.adapter.bluetoothLeScanner

    val repository = (app as DemoApp).repository
    var serviceStateJob: Job? = null

    init {
        //check if ring device is already a companion device
        if(companionDeviceManager.myAssociations.isNotEmpty()) {
            val associatedDevice = companionDeviceManager.myAssociations[0]
            val ringDevice = SmartRingDevice(associatedDevice.displayName.toString(), associatedDevice.deviceMacAddress.toString())
            updateRingDevice(ringDevice)
            updateScanState(HomeState.ScanState.SCAN_STATE_FOUND)
        }

        serviceStateJob?.cancel()

        serviceStateJob = viewModelScope.launch {
            repository.state.collect { serviceState ->
                updateServiceState(serviceState)
            }
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


    private fun updateServiceState(serviceState: ServiceState) {
        _state.update { currentState ->
            currentState.copy(serviceState = serviceState)
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


    fun startConnection(ringDevice: SmartRingDevice) {
        addAssociation(ringDevice)
        //TODO(do BLE connection here)
    }



    private fun addAssociation(ringDevice: SmartRingDevice) {
        val association = companionDeviceManager.myAssociations.firstOrNull{ association ->
            association.deviceMacAddress.toString() == ringDevice.address
        }

        if(association != null) return
        else {

            val executor = Executor {it.run()}

            val scanFilter = ScanFilter.Builder()
                .setDeviceName(ringDevice.name)
                .setDeviceAddress(ringDevice.address)
                .build()

            val associationFilter = BluetoothLeDeviceFilter.Builder()
                .setScanFilter(scanFilter)
                .build()

            val request = AssociationRequest.Builder()
                .setSingleDevice(true)
                .addDeviceFilter(associationFilter)
                .build()

            val callback = object: CompanionDeviceManager.Callback() {
                override fun onFailure(p0: CharSequence?) {
                    Log.d("ERROR", p0.toString())
                }

                override fun onAssociationPending(intentSender: IntentSender) {
                    super.onAssociationPending(intentSender)
                    //line of code that displays popup on screen
                    app.startIntentSender(intentSender, null, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_NEW_TASK, 0)
                }

                override fun onAssociationCreated(associationInfo: AssociationInfo) {
                    super.onAssociationCreated(associationInfo)
                    Toast.makeText(app,"Device correctly associated", Toast.LENGTH_SHORT).show()
                }

            }

            companionDeviceManager.associate(request, executor, callback)
        }
    }

}
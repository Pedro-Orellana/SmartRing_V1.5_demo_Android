package com.orellana.smartringdemov1.bluetooth

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.annotation.RequiresPermission
import com.orellana.smartringdemov1.MAC_ADDRESS_INTENT_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionRepository(val context: Context) {


    private var service: DemoConnectionService? = null

    private val _state = MutableStateFlow(ServiceState())
    val state = _state.asStateFlow()

    //state functions
    private fun updateServiceState(serviceState: ServiceState) {
        _state.update { serviceState }
    }

    private var serviceStateJob: Job? = null

    val serviceIntent = Intent(context, DemoConnectionService::class.java)


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            p0: ComponentName?,
            binder: IBinder?
        ) {
            binder?.let {
                service = (it as DemoConnectionService.DemoConnectionBinder).getService()

                serviceStateJob?.cancel()

                serviceStateJob = CoroutineScope(Dispatchers.Main).launch {
                    service?.state?.collect { serviceState ->
                        updateServiceState(serviceState)
                    }
                }

            }

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            serviceStateJob?.cancel()
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connectToService(macAddress: String) {

        if (service == null) {
            if (!serviceIntent.hasExtra(MAC_ADDRESS_INTENT_EXTRA)) {
                serviceIntent.putExtra(MAC_ADDRESS_INTENT_EXTRA, macAddress)
            }

            context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        } else {
            service?.connectRing(macAddress)
        }

    }


    fun disconnectFromService() {
        if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            service?.disconnectRing()
            context.unbindService(serviceConnection)
            service = null
        }
    }



    //LED methods
    fun sendLedData(data: Char) {
        if(context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            service?.sendLedData(data)
        }
    }
}
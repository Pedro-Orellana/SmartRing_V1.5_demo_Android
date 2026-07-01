package com.orellana.smartringdemov1.bluetooth

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.orellana.smartringdemov1.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.orellana.smartringdemov1.MAC_ADDRESS_INTENT_EXTRA
import com.orellana.smartringdemov1.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class DemoConnectionService : Service() {

    lateinit var notificationManager: NotificationManager
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter

    //binding intent
    private var bindingIntent: Intent? = null

    //connection gatt
    private var gatt: BluetoothGatt? = null
    private var rxCharacteristic: BluetoothGattCharacteristic? = null

    //bluetooth connection job
    private var connectionJob: Job? = null

    fun updateAndCancelJob(gatt: BluetoothGatt?, rxCharacteristic: BluetoothGattCharacteristic?) {
        connectionJob?.cancel()
        this.gatt = gatt
        this.rxCharacteristic = rxCharacteristic
    }

    //state
    private val _state = MutableStateFlow(ServiceState())
    val state = _state.asStateFlow()


    //state functions
    private fun updateIsConnected(connectionState: ServiceState.ConnectionState) {
        _state.update { current ->
            current.copy(connectionState = connectionState)
        }
    }

    private fun updateSensorData(
        newXAccel: String,
        newYAccel: String,
        newZAccel: String,
        newYaw: String,
        newPitch: String,
        newRoll: String,
    ) {
        _state.update { currentState ->
            currentState.copy(
                xAccel = newXAccel,
                yAccel = newYAccel,
                zAccel = newZAccel,
                yaw = newYaw,
                pitch = newPitch,
                roll = newRoll
            )
        }
    }


    //custom gatt callback
    val callback = DemoGattCallback(
        updateIsConnected = this::updateIsConnected,
        updateAndCancelJob = this::updateAndCancelJob,
        updateSensorData = this::updateSensorData,
        startForeground = this::onServiceStartForeground
    )



    //override service methods
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onBind(intent: Intent?): IBinder? {
        intent?.let {
            bindingIntent = it
            val macAddress = it.getStringExtra(MAC_ADDRESS_INTENT_EXTRA)
            connectRing(macAddress!!)
            val demoBinder = DemoConnectionBinder()

            return demoBinder
        }
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(30, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        return super.onStartCommand(intent, flags, startId)
    }


    //notification and foreground connection methods
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID, "Foreground Service Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.sensor_icon)
            .setContentTitle("Demo App")
            .setContentText("Smart Ring working in the background")
            .build()
    }

    private fun onServiceStartForeground() {
        startForegroundService(bindingIntent)
    }




    //custom methods

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connectRing(macAddress: String) {
        connectionJob = CoroutineScope(Dispatchers.Main).launch {
            val device = bluetoothAdapter.getRemoteDevice(macAddress)
            updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING)
            gatt = device.connectGatt(
                this@DemoConnectionService,
                true,
                callback
            )

            delay(5000.milliseconds)
            connectionTimeout()

        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnectRing() {
        gatt?.disconnect()
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun connectionTimeout() {
        gatt?.disconnect()
        gatt?.close()
        updateIsConnected(ServiceState.ConnectionState.CONNECTION_STATE_DISCONNECTED)
        connectionJob?.cancel()
        Toast.makeText(this, "Could not connect to ring. Please try again", Toast.LENGTH_SHORT)
            .show()
    }



    //LED methods

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun sendLedData(data: Char) {
        if(gatt != null && rxCharacteristic != null) {
            val byteArray = byteArrayOf(data.code.toByte())
            gatt!!.writeCharacteristic(rxCharacteristic!!, byteArray, BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE)
        }
    }


    //binder class

    inner class DemoConnectionBinder : Binder() {

        fun getService(): DemoConnectionService {
            return this@DemoConnectionService
        }

    }
}

//service state
data class ServiceState(
    val connectionState: ConnectionState = ConnectionState.CONNECTION_STATE_DISCONNECTED,
    val xAccel: String = "0.00",
    val yAccel: String = "0.00",
    val zAccel: String = "0.00",
    val yaw: String = "0.00",
    val pitch: String = "0.00",
    val roll: String = "0.00"
) {
    enum class ConnectionState {
        CONNECTION_STATE_DISCONNECTED,
        CONNECTION_STATE_CONNECTING,
        CONNECTION_STATE_CONNECTED
    }
}
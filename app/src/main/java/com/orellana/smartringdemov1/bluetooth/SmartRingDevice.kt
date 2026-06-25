package com.orellana.smartringdemov1.bluetooth

data class SmartRingDevice(
    val name: String,
    val address: String,
    val isConnected: Boolean = false
)

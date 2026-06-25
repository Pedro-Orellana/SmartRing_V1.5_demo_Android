package com.orellana.smartringdemov1

import android.app.Application
import com.orellana.smartringdemov1.bluetooth.ConnectionRepository

class DemoApp: Application() {

    val repository by lazy {
        ConnectionRepository(this)
    }
}
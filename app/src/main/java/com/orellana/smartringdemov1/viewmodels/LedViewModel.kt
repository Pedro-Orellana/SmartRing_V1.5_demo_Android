package com.orellana.smartringdemov1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orellana.smartringdemov1.DemoApp
import com.orellana.smartringdemov1.bluetooth.ServiceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LedViewModel(app: Application) : AndroidViewModel(app) {

    private val _state = MutableStateFlow(LedScreenState())
    val state = _state.asStateFlow()


    //state mutation methods
    private fun updateServiceState(serviceState: ServiceState) {
        _state.update { currentState ->
            currentState.copy(serviceState = serviceState)
        }
    }

    val repository = (app as DemoApp).repository

    private var stateJob: Job? = null

    //start collecting state from repository on view model initialization
    init {
        stateJob?.cancel()
        stateJob = viewModelScope.launch {
            repository.state.collect { newServiceState ->
                updateServiceState(newServiceState)
            }
        }
    }


    //custom methods
    fun onSendData(data: Char) {
        repository.sendLedData(data)
    }


}


//state class
data class LedScreenState(
    val serviceState: ServiceState = ServiceState()
)
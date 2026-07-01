package com.orellana.smartringdemov1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orellana.smartringdemov1.DemoApp
import com.orellana.smartringdemov1.bluetooth.ServiceState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SensorViewModel(app: Application): AndroidViewModel(app) {

    val repository = (app as DemoApp).repository

    var serviceStateJob: Job? = null

    private val _state = MutableStateFlow(SensorState())


    @OptIn(FlowPreview::class)
//    val state = _state
//        .sample(30.milliseconds)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = _state.value
//        )

    val state = _state.asStateFlow()


    //state mutation functions
    private fun updateServiceState(serviceState: ServiceState) {
        _state.update { currentState ->
            currentState.copy(serviceState = serviceState)
        }
    }


    init {
        serviceStateJob?.cancel()
        serviceStateJob = viewModelScope.launch {
            repository.state.collect { newServiceState ->
                updateServiceState(newServiceState)
            }
        }
    }

}


data class SensorState(
    val serviceState: ServiceState = ServiceState()
)
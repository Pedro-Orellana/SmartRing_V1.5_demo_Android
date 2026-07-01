package com.orellana.smartringdemov1.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.orellana.smartringdemov1.bluetooth.ServiceState
import com.orellana.smartringdemov1.bluetooth.ServiceState.ConnectionState
import com.orellana.smartringdemov1.components.DeviceNotConnectedCard
import com.orellana.smartringdemov1.components.SensorTestComponent

@Composable
fun SensorTestScreen(
    serviceState: ServiceState,
    navigateBackHome: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Sensor testing",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Test the motion tracking of the Smart Ring and see in real time the values for acceleration and Euler angles",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedContent(
                targetState = serviceState.connectionState
            ) { newConnectionState ->

                when(newConnectionState) {
                    ConnectionState.CONNECTION_STATE_CONNECTED -> {
                        SensorTestComponent(
                            xAccel = serviceState.xAccel,
                            yAccel = serviceState.yAccel,
                            zAccel = serviceState.zAccel,
                            yaw = serviceState.yaw,
                            pitch = serviceState.pitch,
                            roll = serviceState.roll
                        )
                    }
                    else -> DeviceNotConnectedCard { navigateBackHome() }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SensorTestScreenPreview() {
    val serviceState = ServiceState()
    SensorTestScreen(serviceState){}
}
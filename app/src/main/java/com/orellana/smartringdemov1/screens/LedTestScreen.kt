package com.orellana.smartringdemov1.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orellana.smartringdemov1.bluetooth.ServiceState
import com.orellana.smartringdemov1.components.DeviceNotConnectedCard
import com.orellana.smartringdemov1.components.LedTestComponent

@Composable
fun LedTestScreen(
    connectionState: ServiceState.ConnectionState,
    onSendData: (Char) -> Unit,
    navigateBackHome: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "LED testing",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Here you can do a basic test of the on-board LED through Bluetooth",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedContent(
                targetState = connectionState
            ) { newConnectedState ->
                when (newConnectedState) {
                    ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED -> {
                        LedTestComponent(onSendData)
                    }
                    else -> DeviceNotConnectedCard(navigateBackHome)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LedTestScreenPreview() {
    val connectionState = ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED
    LedTestScreen(connectionState, {}) {}
}
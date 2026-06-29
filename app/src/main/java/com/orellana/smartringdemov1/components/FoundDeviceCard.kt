package com.orellana.smartringdemov1.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orellana.smartringdemov1.bluetooth.ServiceState
import com.orellana.smartringdemov1.bluetooth.SmartRingDevice

@Composable
fun FoundDeviceCard(
    ringDevice: SmartRingDevice,
    connectionState: ServiceState.ConnectionState,
    onConnectDevice: (SmartRingDevice) -> Unit,
    onDisconnectDevice: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "Registered Smart Ring Device found",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Device Name:",
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = ringDevice.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Device Address:",
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = ringDevice.address,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            AnimatedContent(
                targetState = connectionState
            ) { connState ->
                when (connState) {
                    ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING -> ConnectionLoading()
                    ServiceState.ConnectionState.CONNECTION_STATE_CONNECTED -> RingInteraction(
                        ringDevice,
                        true,
                        onDisconnectDevice = onDisconnectDevice
                    )

                    ServiceState.ConnectionState.CONNECTION_STATE_DISCONNECTED -> RingInteraction(
                        ringDevice,
                        false,
                        onConnectDevice = onConnectDevice
                    )

                }
            }

        }

    }
}


@Composable
fun ConnectionLoading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .aspectRatio(1f),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Text(
            text = "Connecting...",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}


@Composable
fun RingInteraction(
    ringDevice: SmartRingDevice,
    isConnected: Boolean,
    onConnectDevice: ((SmartRingDevice) -> Unit)? = null,
    onDisconnectDevice: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isConnected) "Connected" else "Disconnected",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = if (isConnected) Color.Green else Color.Red,
                    radius = size.minDimension / 4
                )
            }
        }

        OutlinedButton(
            onClick = {
                if (isConnected) {
                    onDisconnectDevice?.invoke()
                } else {
                    onConnectDevice?.invoke(ringDevice)
                }
            },
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = if (isConnected) "Disconnect" else "Connect"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoundDeviceCardPreview() {
    val ringDevice = SmartRingDevice(
        name = "Demo device",
        address = "AA:BB:CC:DD:EE:FF"
    )
    FoundDeviceCard(ringDevice, ServiceState.ConnectionState.CONNECTION_STATE_CONNECTING, {}) {}
}
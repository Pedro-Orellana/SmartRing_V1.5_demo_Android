package com.orellana.smartringdemov1.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoDeviceRegisteredCard( onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "You have not registered a device with this app yet",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light
        )

        OutlinedButton (onClick = onClick) {
            Text(
                text = "Start Scanning"
            )
        }
    }

}


@Preview (showBackground = true)
@Composable
fun NoDeviceRegisteredCardPreview() {
    NoDeviceRegisteredCard{}
}
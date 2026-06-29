package com.orellana.smartringdemov1.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DeviceNotConnectedCard(
    navigateBackHome: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Smart Ring is not connected yet",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "Please connect to ring before you try to use this feature",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            OutlinedButton(
                onClick = navigateBackHome
            ) {
                Text(
                    text = "Go to Home Screen"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DeviceNotConnectedCardPreview() {
    DeviceNotConnectedCard {}
}
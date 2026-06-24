package com.orellana.smartringdemov1.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orellana.smartringdemov1.components.NoDeviceRegisteredCard

@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Welcome to the Demo app!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "The purpose of this app is to demonstrate the basic functionality of the SmartRing V1.5",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Below you will be able to find your device (if you have any registered) and its current status:"
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            NoDeviceRegisteredCard()

        }


    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
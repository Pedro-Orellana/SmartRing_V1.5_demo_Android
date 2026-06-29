package com.orellana.smartringdemov1.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LedTestComponent(
    onSendData: (Char) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(12.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Try the following buttons to test different colors on the LED",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )


            Button(
                onClick = {onSendData('r')},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 8.dp)
            ) {
                Text("Red")
            }

            Button(
                onClick = {onSendData('g')},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 8.dp)
            ) {
                Text("Green")
            }

            Button(
                onClick = {onSendData('b')},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),  modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 8.dp)
            ) {
                Text("Blue")
            }


            OutlinedButton(
                onClick = {onSendData('o')},
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 20.dp)
            ) {
                Text("Turn off")
            }



        }
    }
}



@Preview(showBackground = true)
@Composable
fun LedTestComponentPreview() {
    LedTestComponent{}
}
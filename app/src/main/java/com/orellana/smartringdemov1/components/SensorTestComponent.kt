package com.orellana.smartringdemov1.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SensorTestComponent(
    xAccel: Float,
    yAccel: Float,
    zAccel: Float,
    yaw: Float,
    pitch: Float,
    roll: Float
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
                text = "Acceleration data",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Acceleration X axis:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$xAccel m/s²",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Acceleration Y axis:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$yAccel m/s²"
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Acceleration Z axis:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$zAccel m/s²"
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )


            Text(
                text = "Angle data",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Yaw angle:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$yaw º"
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Pitch angle:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$pitch º"
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp)
            ) {
                Text(
                    text = "Roll angle:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "$roll º"
                )
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun SensorTestComponentPreview() {

    SensorTestComponent(2.0f, 3.3f, 4.4f, 120f, 45f, 270f)
}
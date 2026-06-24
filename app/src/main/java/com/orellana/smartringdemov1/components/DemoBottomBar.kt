package com.orellana.smartringdemov1.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.orellana.smartringdemov1.R
import com.orellana.smartringdemov1.screens.ScreenDestinations

@Composable
fun DemoBottomBar(currentRoute: String, onItemClick: (String) -> Unit) {
    BottomAppBar {
        NavigationBarItem(
            selected = currentRoute == ScreenDestinations.Home.name,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home_icon),
                    contentDescription = "Home Icon"
                )
            },
            label = { Text(text = "Home") },
            onClick = { onItemClick(ScreenDestinations.Home.name) }
        )

        NavigationBarItem(
            selected = currentRoute == ScreenDestinations.Led.name,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.led_icon),
                    contentDescription = "Led Icon"
                )
            },
            label = { Text(text = "Led Test") },
            onClick = { onItemClick(ScreenDestinations.Led.name) }
        )

        NavigationBarItem(
            selected = currentRoute == ScreenDestinations.Sensor.name,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.sensor_icon),
                    contentDescription = "Home Icon"
                )
            },
            label = { Text(text = "Sensor Test") },
            onClick = { onItemClick(ScreenDestinations.Sensor.name) }
        )




    }
}
package com.orellana.smartringdemov1.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.orellana.smartringdemov1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoAppBar(title: String, canShowNavIcon: Boolean, onNavIconClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (canShowNavIcon) {
                IconButton(onClick = onNavIconClick) {
                    Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = "Back arrow")
                }
            }
        }
    )
}
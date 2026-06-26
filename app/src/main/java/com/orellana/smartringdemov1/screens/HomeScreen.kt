package com.orellana.smartringdemov1.screens


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orellana.smartringdemov1.bluetooth.SmartRingDevice
import com.orellana.smartringdemov1.components.FoundDeviceCard
import com.orellana.smartringdemov1.components.NoDeviceRegisteredCard
import com.orellana.smartringdemov1.components.ScanningCard
import com.orellana.smartringdemov1.viewmodels.HomeState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    snackBarHostState: SnackbarHostState,
    homeState: HomeState,
    onConnectDevice: (SmartRingDevice) -> Unit,
    onDisconnectDevice: () -> Unit,
    startScanning: () -> Unit,
    ) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val isScanGranted = results[Manifest.permission.BLUETOOTH_SCAN] ?: false
        val isConnectGranted = results[Manifest.permission.BLUETOOTH_CONNECT] ?: false

        if (isConnectGranted && isScanGranted) {
            //start scanning here
            startScanning()
        } else {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = "Necessary permissions not granted",
                    actionLabel = "Go to Settings",
                    duration = SnackbarDuration.Short
                )

                when (snackBarResult) {
                    SnackbarResult.ActionPerformed -> {/*go to settings*/
                    }

                    SnackbarResult.Dismissed -> {}
                }
            }

        }
    }



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

            AnimatedContent(
                targetState = homeState.scanState
            ) { scanState ->
                when (scanState) {
                    HomeState.ScanState.SCAN_STATE_IDLE -> {
                        NoDeviceRegisteredCard {
                            if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                bluetoothPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.BLUETOOTH_SCAN,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    )
                                )
                            } else {
                                startScanning()
                            }
                        }
                    }

                    HomeState.ScanState.SCAN_STATE_SCANNING -> {
                        ScanningCard()
                    }

                    HomeState.ScanState.SCAN_STATE_FOUND -> {
                        homeState.newDevice?.let {
                            FoundDeviceCard(
                                ringDevice = it,
                                isConnected = homeState.serviceState.isConnected,
                                onConnectDevice = onConnectDevice,
                                onDisconnectDevice = onDisconnectDevice
                            )
                        }
                    }
                }
            }


        }


    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val snackBarState = remember { SnackbarHostState() }
    val ringDevice = SmartRingDevice(name = "Demo device", "AA:BB:CC:DD:EE:FF")
    val homeState = HomeState(scanState = HomeState.ScanState.SCAN_STATE_FOUND, newDevice = ringDevice)
    HomeScreen(
        snackBarHostState = snackBarState,
        homeState = homeState,
        onConnectDevice = {},
        onDisconnectDevice = {}
    ) {}
}
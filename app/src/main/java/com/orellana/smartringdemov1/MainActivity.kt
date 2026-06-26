package com.orellana.smartringdemov1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orellana.smartringdemov1.components.DemoAppBar
import com.orellana.smartringdemov1.components.DemoBottomBar
import com.orellana.smartringdemov1.screens.HomeScreen
import com.orellana.smartringdemov1.screens.LedTestScreen
import com.orellana.smartringdemov1.screens.ScreenDestinations
import com.orellana.smartringdemov1.screens.SensorTestScreen
import com.orellana.smartringdemov1.ui.theme.SmartRingDemoV1Theme
import com.orellana.smartringdemov1.viewmodels.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRingDemoV1Theme {
                MainContainer()
            }
        }
    }
}


@Composable
fun MainContainer() {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value

    val snackBarHostState = remember { SnackbarHostState() }

    val currentRoute = currentDestination?.destination?.route ?: ScreenDestinations.Home.name

    val appBarTitle = when (currentRoute) {
        ScreenDestinations.Home.name -> ScreenDestinations.Home.title
        ScreenDestinations.Led.name -> ScreenDestinations.Led.title
        ScreenDestinations.Sensor.name -> ScreenDestinations.Sensor.title
        else -> ScreenDestinations.Home.title
    }

    val canShowNavIcon: Boolean =
        currentDestination?.destination?.route != ScreenDestinations.Home.name

    Scaffold(
        topBar = {
            DemoAppBar(appBarTitle, canShowNavIcon) {
                navController.popBackStack(
                    ScreenDestinations.Home.name, false
                )
            }
        },
        bottomBar = {
            DemoBottomBar(currentRoute) { destination ->
                navController.navigate(
                    destination
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenDestinations.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ScreenDestinations.Home.name) {
                val viewModel: HomeViewModel = viewModel()
                val homeState = viewModel.state.collectAsStateWithLifecycle().value
                HomeScreen(
                    startScanning = viewModel::startScanning,
                    snackBarHostState = snackBarHostState,
                    homeState = homeState,
                    onConnectDevice = { viewModel.startConnection(it) },
                    onDisconnectDevice = { viewModel.disconnectDevice() }
                )
            }

            composable(route = ScreenDestinations.Led.name) {
                LedTestScreen()
            }

            composable(route = ScreenDestinations.Sensor.name) {
                SensorTestScreen()
            }
        }

    }

}
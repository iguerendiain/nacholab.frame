package nacholab.frame.fullclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nacholab.frame.fullclient.ui.connection.ConnectionScreen
import nacholab.frame.fullclient.ui.mainconfig.MainConfigScreen

@Composable
fun FullClientNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(FullClientDestination.ConnectionSetup.route) {
            ConnectionScreen(
                onConnected = {
                    navController.navigate(FullClientDestination.MainConfig.route) {
                        popUpTo(FullClientDestination.ConnectionSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(FullClientDestination.MainConfig.route) {
            MainConfigScreen(
                onServerChanged = {
                    navController.navigate(FullClientDestination.ConnectionSetup.route) {
                        popUpTo(FullClientDestination.MainConfig.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

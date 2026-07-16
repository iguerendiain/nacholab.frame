package nacholab.frame.fullclient.ui.navigation

sealed class FullClientDestination(val route: String) {

    object ConnectionSetup : FullClientDestination("connection_setup")

    object MainConfig : FullClientDestination("main_config")

}

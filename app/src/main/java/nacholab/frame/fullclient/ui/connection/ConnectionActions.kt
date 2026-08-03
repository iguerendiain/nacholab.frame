package nacholab.frame.fullclient.ui.connection

sealed class ConnectionActions {

    data class SetHost(val host: String) : ConnectionActions()

    data class SetPort(val port: String) : ConnectionActions()

    object Connect : ConnectionActions()

    data class QrCodeScanned(val rawValue: String) : ConnectionActions()

}

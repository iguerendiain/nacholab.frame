package nacholab.frame.fullclient.ui.connection

data class ConnectionState(
    val host: String,
    val port: String,
    val hostError: Boolean,
    val portError: Boolean,
    val isSaving: Boolean
) {
    val isFormValid: Boolean
        get() = host.isNotBlank() && port.isNotBlank() && !hostError && !portError

    companion object {
        val DEFAULT = ConnectionState(
            host = "",
            port = "",
            hostError = false,
            portError = false,
            isSaving = false
        )
    }
}

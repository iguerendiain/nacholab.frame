package nacholab.frame.server.ui

sealed class ServerAppUIEvents {

    object NavigateToMainGallery: ServerAppUIEvents()
    object RequestFolder: ServerAppUIEvents()

}
package nacholab.frame.ui.server

sealed class ServerAppUIEvents {

    object NavigateToMainGallery: ServerAppUIEvents()
    object RequestFolder: ServerAppUIEvents()

}
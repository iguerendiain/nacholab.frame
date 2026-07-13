package nacholab.frame.ui.server

import androidx.activity.ComponentActivity

sealed class ServerAppActions {

    class LoadMedia(val activity: ComponentActivity): ServerAppActions()
    class SetBrightness(val brightness: Float): ServerAppActions()
    class SetVolume(val volume: Float): ServerAppActions()
    class SetMuted(val isMuted: Boolean): ServerAppActions()
    object ToggleMuted: ServerAppActions()
    class SetPlaying(val isPlaying: Boolean): ServerAppActions()
    object TogglePlaying: ServerAppActions()
    class SetVideoPosition(val position: Float): ServerAppActions()
    object Sleep: ServerAppActions()
    object Wakeup: ServerAppActions()

}
package nacholab.frame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import nacholab.frame.data.LoadingState
import nacholab.frame.ui.server.MainGallery
import nacholab.frame.ui.server.ServerAppActions
import nacholab.frame.ui.server.ServerAppViewModel
import nacholab.frame.ui.theme.Black
import nacholab.frame.ui.theme.NacholabFrameServerTheme
import nacholab.frame.ui.theme.White
import nacholab.frame.utils.BrightnessLaunchEffect
import nacholab.frame.utils.BrightnessUtils
import nacholab.frame.utils.FullscreenEffect

@AndroidEntryPoint
class ServerActivity : ComponentActivity() {

    private val vm: ServerAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.onAction(ServerAppActions.LoadMedia(this))
        vm.onAction(ServerAppActions.SetBrightness(BrightnessUtils.getScreenBrightness(this)))

        enableEdgeToEdge()
        setContent {
            val state by vm.state.collectAsState()

            FullscreenEffect()
            BrightnessLaunchEffect(state.brightness)

            NacholabFrameServerTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Black),
                    contentAlignment = Alignment.Center
                ) {
                    when (state.loadingState) {
                        LoadingState.Loading -> CircularProgressIndicator(modifier = Modifier.size(72.dp), color = White)
                        is LoadingState.NetworkError -> Text(
                                    text = "Error de red: ${(state.loadingState as LoadingState.NetworkError).exception.message}"
                        )
                        is LoadingState.ServerError -> {
                            val error = (state.loadingState as LoadingState.ServerError)
                            val code = error.code
                            val message = error.message
                            Text(
                                text = "Error de servidor: [$code] - $message"
                            )
                        }
                        is LoadingState.Success -> MainGallery(
                            mediaList = state.currentGallery,
                            imageTimeout = 3,
                            currentBrightness = state.brightness,
                            isPlaying = true,//state.isPlaying,
                            isMuted = state.isMuted,
                            currentVolume = state.volume,
                            sleepMode = state.sleepMode,
                            sleepFrom = state.sleepFrom,
                            sleepTo = state.sleepTo,
                            ampm = state.ampm,
                            decorations = state.decorations,
                            onAction = vm::onAction,
                        )
                    }
                }
            }
        }
    }
}

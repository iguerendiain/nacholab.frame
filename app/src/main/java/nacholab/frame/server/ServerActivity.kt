package nacholab.frame.server

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import nacholab.frame.ui.model.LoadingState
import nacholab.frame.server.ui.MainGallery
import nacholab.frame.server.ui.ServerAppActions
import nacholab.frame.server.ui.ServerAppViewModel
import nacholab.frame.ui.utils.BrightnessLaunchEffect
import nacholab.frame.ui.utils.BrightnessUtils
import nacholab.frame.ui.utils.FullscreenEffect
import javax.inject.Inject

@AndroidEntryPoint
class ServerActivity : ComponentActivity() {

    private val vm: ServerAppViewModel by viewModels()

    @Inject
    lateinit var remoteControlServer: RemoteControlServer

    override fun onResume() {
        super.onResume()
        remoteControlServer.startServer()
        remoteControlServer.onServerConfigReceived = { config ->
            vm.onAction(ServerAppActions.ReceiveServerConfig(config))
        }
    }

    override fun onPause() {
        remoteControlServer.stopServer()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.onAction(ServerAppActions.LoadMedia(this))
        vm.onAction(ServerAppActions.SetBrightness(BrightnessUtils.getScreenBrightness(this)))
        vm.onAction(ServerAppActions.StartMinuteClock)

        enableEdgeToEdge()
        setContent {
            val state by vm.state.collectAsState()

            FullscreenEffect()
            BrightnessLaunchEffect(state.brightness)

            MaterialTheme {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Black),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    when (state.loadingState) {
                        LoadingState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.Companion.size(
                                72.dp
                            ), color = White
                        )

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
                            currentMinute = state.minuteClock,
                            onAction = vm::onAction,
                        )
                    }
                }
            }
        }
    }
}
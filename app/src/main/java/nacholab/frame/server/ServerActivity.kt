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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nacholab.frame.domain.model.ServerMessage
import nacholab.frame.server.domain.usecase.GetServerConfigUseCase
import nacholab.frame.server.domain.usecase.SaveServerConfigUseCase
import nacholab.frame.ui.model.LoadingState
import nacholab.frame.server.ui.MainGallery
import nacholab.frame.server.ui.ServerAppActions
import nacholab.frame.server.ui.ServerAppUIEvents
import nacholab.frame.server.ui.ServerAppViewModel
import nacholab.frame.ui.utils.BrightnessLaunchEffect
import nacholab.frame.ui.utils.BrightnessUtils
import nacholab.frame.ui.utils.FullscreenEffect
import nacholab.frame.utils.NetworkUtils
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class ServerActivity : ComponentActivity() {

    private val vm: ServerAppViewModel by viewModels()

    @Inject
    lateinit var remoteControlServer: RemoteControlServer

    @Inject
    lateinit var saveServerConfigUseCase: SaveServerConfigUseCase

    @Inject
    lateinit var getServerConfigUseCase: GetServerConfigUseCase


    override fun onResume() {
        super.onResume()
        val port = Random.nextInt(4747,84747)
        vm.onAction(ServerAppActions.SetServerPort(port))
        remoteControlServer.startServer(port)
        remoteControlServer.onMessageReceived = { message ->
            when (message) {
                is ServerMessage.SendConfig -> {
                    saveServerConfigUseCase(message.payload)
                    vm.onAction(ServerAppActions.ReceiveServerConfig(message.payload))
                }
                is ServerMessage.GetConfig -> Unit
                is ServerMessage.GetConfigResponse -> Unit
                ServerMessage.ReloadPlaylist -> Unit
            }
        }
    }

    override fun onPause() {
        remoteControlServer.stopServer()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiEventBus.collect { event ->
                    when (event) {
                        ServerAppUIEvents.RequestReload -> vm.onAction(ServerAppActions.LoadMedia(this@ServerActivity))
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                NetworkUtils.currentIpAddress(this@ServerActivity).collect { ip ->
                    vm.onAction(ServerAppActions.SetServerHost(ip ?: "localhost"))
                }
            }
        }

        getServerConfigUseCase()?.let { vm.onAction(ServerAppActions.ReceiveServerConfig(it)) }

        vm.onAction(ServerAppActions.LoadMedia(this))
        vm.onAction(ServerAppActions.SetBrightness(BrightnessUtils.getScreenBrightness(this)))
        vm.onAction(ServerAppActions.StartMinuteClock)

        mainContent()
    }

    private fun mainContent(){
        enableEdgeToEdge()
        setContent {
            val state by vm.state.collectAsState()

            FullscreenEffect()
            BrightnessLaunchEffect(state.brightness)

            MaterialTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Black),
                    contentAlignment = Alignment.Center
                ) {
                    when (state.loadingState) {
                        LoadingState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.size(
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
                            isPlaying = state.isPlaying,
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
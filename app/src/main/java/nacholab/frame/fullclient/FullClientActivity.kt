package nacholab.frame.fullclient

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import nacholab.frame.fullclient.domain.usecase.HasConnectionConfigUseCase
import nacholab.frame.fullclient.domain.usecase.ParseConnectionUriUseCase
import nacholab.frame.fullclient.domain.usecase.SaveConnectionConfigUseCase
import nacholab.frame.fullclient.ui.navigation.FullClientDestination
import nacholab.frame.fullclient.ui.navigation.FullClientNavHost
import nacholab.frame.theme.NacholabFrameTheme
import javax.inject.Inject

@AndroidEntryPoint
class FullClientActivity : ComponentActivity() {

    @Inject
    lateinit var hasConnectionConfigUseCase: HasConnectionConfigUseCase

    @Inject
    lateinit var parseConnectionUriUseCase: ParseConnectionUriUseCase

    @Inject
    lateinit var saveConnectionConfigUseCase: SaveConnectionConfigUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isSystemInDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
        enableEdgeToEdge(
            statusBarStyle = if (isSystemInDarkTheme) {
                SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
            } else {
                SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
            }
        )

        applyConnectionDeepLink(intent)

        val startDestination = if (hasConnectionConfigUseCase()) {
            FullClientDestination.MainConfig.route
        } else {
            FullClientDestination.ConnectionSetup.route
        }

        setContent {
            NacholabFrameTheme {
                FullClientNavHost(startDestination = startDestination)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Re-run onCreate's startup logic so a re-scanned QR (app already running,
        // singleTask brings this instance back) picks up the new connection config.
        recreate()
    }

    private fun applyConnectionDeepLink(intent: Intent) {
        val uri = intent.data?.toString() ?: return
        val config = parseConnectionUriUseCase(uri) ?: return
        saveConnectionConfigUseCase(config)
    }
}

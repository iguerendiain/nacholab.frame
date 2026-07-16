package nacholab.frame.fullclient

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import nacholab.frame.fullclient.domain.usecase.HasConnectionConfigUseCase
import nacholab.frame.fullclient.ui.navigation.FullClientDestination
import nacholab.frame.fullclient.ui.navigation.FullClientNavHost
import nacholab.frame.theme.NacholabFrameTheme
import javax.inject.Inject

@AndroidEntryPoint
class FullClientActivity : ComponentActivity() {

    @Inject
    lateinit var hasConnectionConfigUseCase: HasConnectionConfigUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Every screen opens with a full-bleed brand-colored header behind the status bar.
        // That header is dark navy in light theme and light periwinkle in dark theme, i.e.
        // the inverse of the system setting, so the status bar icon style must invert too.
        val isSystemInDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
        enableEdgeToEdge(
            statusBarStyle = if (isSystemInDarkTheme) {
                SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
            } else {
                SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
            }
        )

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
}

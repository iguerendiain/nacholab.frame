package nacholab.frame.fullclient.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.theme.NacholabFrameTheme

/**
 * Draws full-bleed behind the status bar: the [Surface] background is not inset, only the
 * text content is pushed down via [Modifier.statusBarsPadding], so callers must not add their
 * own top window-inset padding above this composable.
 */
@Composable
fun FullClientHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullClientHeaderPreview() {
    NacholabFrameTheme {
        FullClientHeader(
            title = "Connect to server",
            subtitle = "Enter the IP address and port of the device running the Nacholab Frame server."
        )
    }
}

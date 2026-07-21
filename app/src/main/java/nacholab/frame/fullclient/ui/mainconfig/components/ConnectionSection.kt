package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.theme.MonospaceLabel

@Composable
fun ConnectionSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Connected to ${state.host}:${state.port}",
            style = MonospaceLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )

        OutlinedTextField(
            value = state.frameName,
            onValueChange = { onAction(MainConfigActions.SetFrameName(it)) },
            label = { Text("Frame name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Auto sync",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = state.autoSync,
                onCheckedChange = { onAction(MainConfigActions.SetAutoSync(it)) }
            )
        }
    }
}

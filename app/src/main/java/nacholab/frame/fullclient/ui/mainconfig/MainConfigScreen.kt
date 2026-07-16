package nacholab.frame.fullclient.ui.mainconfig

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import nacholab.frame.fullclient.ui.components.FullClientHeader
import nacholab.frame.theme.MonospaceLabel
import nacholab.frame.fullclient.ui.util.withoutTop

@Composable
fun MainConfigScreen(
    onServerChanged: () -> Unit,
    viewModel: MainConfigViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEventBus.collectLatest { event ->
            when (event) {
                MainConfigUiEvents.NavigateToConnectionSetup -> onServerChanged()
            }
        }
    }

    MainConfigScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MainConfigScreenContent(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FullClientHeader(title = "Configuration")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues.withoutTop())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                HorizontalDivider()

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

                Button(
                    onClick = { onAction(MainConfigActions.SaveSettings) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save settings")
                }

                OutlinedButton(
                    onClick = { onAction(MainConfigActions.ChangeServer) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change server")
                }
            }
        }
    }
}

package nacholab.frame.fullclient.ui.connection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import nacholab.frame.fullclient.ui.common.components.FullClientHeader
import nacholab.frame.theme.NacholabFrameTheme
import nacholab.frame.utils.withoutTop

@Composable
fun ConnectionScreen(
    onConnected: () -> Unit,
    viewModel: ConnectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEventBus.collectLatest { event ->
            when (event) {
                ConnectionUiEvents.NavigateToMainConfig -> onConnected()
            }
        }
    }

    ConnectionScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ConnectionScreenContent(
    state: ConnectionState,
    onAction: (ConnectionActions) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FullClientHeader(
                title = "Connect to server",
                subtitle = "Enter the IP address and port of the device running the Nacholab Frame server."
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues.withoutTop())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.host,
                    onValueChange = { onAction(ConnectionActions.SetHost(it)) },
                    label = { Text("IP address / host") },
                    isError = state.hostError,
                    supportingText = {
                        if (state.hostError) Text("Enter a valid IP address or hostname")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.port,
                    onValueChange = { onAction(ConnectionActions.SetPort(it)) },
                    label = { Text("Port") },
                    isError = state.portError,
                    supportingText = {
                        if (state.portError) Text("Enter a valid port between 1 and 65535")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { onAction(ConnectionActions.Connect) },
                    enabled = !state.isSaving,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isSaving) "Connecting..." else "Connect")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectionScreenContentPreview() {
    NacholabFrameTheme {
        ConnectionScreenContent(
            state = ConnectionState.DEFAULT,
            onAction = {}
        )
    }
}

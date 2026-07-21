package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nacholab.frame.fullclient.ui.common.components.FullClientHeader
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.utils.withoutTop

@Composable
fun MainConfigScreenContent(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FullClientHeader(title = "Configuration")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues.withoutTop()),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { ConnectionSection(state = state, onAction = onAction) }
                item { HorizontalDivider() }

                item { PlaybackSection(state = state, onAction = onAction) }
                item { HorizontalDivider() }

                item { SleepTimerSection(state = state, onAction = onAction) }
                item { HorizontalDivider() }

                item { MainUiSection(state = state, onAction = onAction) }
                item { HorizontalDivider() }

                decorationsSection(state = state, onAction = onAction)
                item { HorizontalDivider() }

                item { ActionButtonsSection(onAction = onAction) }
            }
        }
    }
}

package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import nacholab.frame.fullclient.ui.mainconfig.MainConfigUiEvents
import nacholab.frame.fullclient.ui.mainconfig.MainConfigViewModel

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


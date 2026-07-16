package nacholab.frame.fullclient.ui.mainconfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nacholab.frame.fullclient.domain.usecase.ClearConnectionConfigUseCase
import nacholab.frame.fullclient.domain.usecase.GetConnectionConfigUseCase
import javax.inject.Inject

@HiltViewModel
class MainConfigViewModel @Inject constructor(
    private val getConnectionConfigUseCase: GetConnectionConfigUseCase,
    private val clearConnectionConfigUseCase: ClearConnectionConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainConfigState.DEFAULT)
    val state = _state.asStateFlow()

    private val _uiEventBus = MutableSharedFlow<MainConfigUiEvents>(extraBufferCapacity = 1)
    val uiEventBus: SharedFlow<MainConfigUiEvents> = _uiEventBus.asSharedFlow()

    init {
        val config = getConnectionConfigUseCase()
        _state.update { it.copy(host = config?.host.orEmpty(), port = config?.port) }
    }

    fun onAction(action: MainConfigActions) {
        when (action) {
            is MainConfigActions.SetFrameName -> _state.update { it.copy(frameName = action.frameName) }
            is MainConfigActions.SetAutoSync -> _state.update { it.copy(autoSync = action.autoSync) }
            MainConfigActions.SaveSettings -> saveSettings()
            MainConfigActions.ChangeServer -> changeServer()
        }
    }

    private fun saveSettings() {
        // TODO: persist configuration fields once defined
    }

    private fun changeServer() {
        clearConnectionConfigUseCase()
        viewModelScope.launch {
            _uiEventBus.emit(MainConfigUiEvents.NavigateToConnectionSetup)
        }
    }

}

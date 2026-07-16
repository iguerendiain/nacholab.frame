package nacholab.frame.fullclient.ui.connection

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
import nacholab.frame.fullclient.domain.model.ConnectionConfig
import nacholab.frame.fullclient.domain.usecase.SaveConnectionConfigUseCase
import nacholab.frame.fullclient.domain.usecase.ValidateHostUseCase
import nacholab.frame.fullclient.domain.usecase.ValidatePortUseCase
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val saveConnectionConfigUseCase: SaveConnectionConfigUseCase,
    private val validateHostUseCase: ValidateHostUseCase,
    private val validatePortUseCase: ValidatePortUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConnectionState.DEFAULT)
    val state = _state.asStateFlow()

    private val _uiEventBus = MutableSharedFlow<ConnectionUiEvents>(extraBufferCapacity = 1)
    val uiEventBus: SharedFlow<ConnectionUiEvents> = _uiEventBus.asSharedFlow()

    fun onAction(action: ConnectionActions) {
        when (action) {
            is ConnectionActions.SetHost -> _state.update {
                it.copy(host = action.host, hostError = false)
            }

            is ConnectionActions.SetPort -> _state.update {
                it.copy(port = action.port, portError = false)
            }

            ConnectionActions.Connect -> connect()
        }
    }

    private fun connect() {
        val currentState = state.value
        val isHostValid = validateHostUseCase(currentState.host)
        val isPortValid = validatePortUseCase(currentState.port)

        if (!isHostValid || !isPortValid) {
            _state.update { it.copy(hostError = !isHostValid, portError = !isPortValid) }
            return
        }

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            saveConnectionConfigUseCase(
                ConnectionConfig(host = currentState.host, port = currentState.port.toInt())
            )
            _state.update { it.copy(isSaving = false) }
            _uiEventBus.emit(ConnectionUiEvents.NavigateToMainConfig)
        }
    }

}

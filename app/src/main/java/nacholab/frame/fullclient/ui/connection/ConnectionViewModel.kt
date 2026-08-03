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
import nacholab.frame.fullclient.domain.usecase.ParseConnectionUriUseCase
import nacholab.frame.fullclient.domain.usecase.SaveConnectionConfigUseCase
import nacholab.frame.fullclient.domain.usecase.ValidateHostUseCase
import nacholab.frame.fullclient.domain.usecase.ValidatePortUseCase
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val saveConnectionConfigUseCase: SaveConnectionConfigUseCase,
    private val validateHostUseCase: ValidateHostUseCase,
    private val validatePortUseCase: ValidatePortUseCase,
    private val parseConnectionUriUseCase: ParseConnectionUriUseCase
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

            is ConnectionActions.QrCodeScanned -> onQrCodeScanned(action.rawValue)
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

        saveConfigAndNavigate(
            ConnectionConfig(host = currentState.host, port = currentState.port.toInt())
        )
    }

    private fun onQrCodeScanned(rawValue: String) {
        val config = parseConnectionUriUseCase(rawValue)
        if (config == null) {
            _state.update { it.copy(qrError = true) }
            return
        }

        _state.update {
            it.copy(
                host = config.host,
                port = config.port.toString(),
                hostError = false,
                portError = false,
                qrError = false
            )
        }

        saveConfigAndNavigate(config)
    }

    private fun saveConfigAndNavigate(config: ConnectionConfig) {
        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            saveConnectionConfigUseCase(config)
            _state.update { it.copy(isSaving = false) }
            _uiEventBus.emit(ConnectionUiEvents.NavigateToMainConfig)
        }
    }

}

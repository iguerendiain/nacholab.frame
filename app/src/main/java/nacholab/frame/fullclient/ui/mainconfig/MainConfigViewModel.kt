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
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI
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
        _state.update { it.copy(host = config?.host.orEmpty(), port = config?.port?:state.value.port) }
    }

    fun onAction(action: MainConfigActions) {
        when (action) {
            is MainConfigActions.SetFrameName -> _state.update { it.copy(frameName = action.frameName) }
            is MainConfigActions.SetAutoSync -> _state.update { it.copy(autoSync = action.autoSync) }

            is MainConfigActions.SetMediaItemTime -> _state.update {
                it.copy(mediaItemTime = action.mediaItemTime, mediaItemTimeError = false)
            }

            is MainConfigActions.SetReshuffleAfterPlaylistFinish -> _state.update {
                it.copy(reshuffleAfterPlaylistFinish = action.reshuffle)
            }

            is MainConfigActions.SetAutomaticallyAdvanceMedia -> _state.update {
                it.copy(automcaticallyAdvanceMedia = action.advance)
            }

            is MainConfigActions.SetSleepTimerFrom -> _state.update {
                it.copy(sleepTimerFrom = action.sleepTimerFrom, sleepTimerFromError = false)
            }

            is MainConfigActions.SetSleepTimerTo -> _state.update {
                it.copy(sleepTimerTo = action.sleepTimerTo, sleepTimerToError = false)
            }

            is MainConfigActions.SetImageScaling -> _state.update { it.copy(imageScaling = action.scaling) }
            is MainConfigActions.SetVideoScaling -> _state.update { it.copy(videoScaling = action.scaling) }

            is MainConfigActions.SetDirectorySortingEnabled -> _state.update {
                it.copy(directorySortEnabled = action.enabled)
            }

            is MainConfigActions.SetSortType -> _state.update { it.copy(sortType = action.sortType) }
            is MainConfigActions.SetDirSortType -> _state.update { it.copy(dirSortType = action.dirSortType) }
            is MainConfigActions.SetMainUIHideType -> _state.update { it.copy(mainUIHideType = action.hideType) }

            is MainConfigActions.SetMainUIHideTimeout -> _state.update {
                it.copy(mainUIHideTimeout = action.hideTimeout)
            }

            is MainConfigActions.SetDecorationDraftKind -> updateDraft { it.copy(kind = action.kind) }
            is MainConfigActions.SetDecorationDraftPosition -> updateDraft { it.copy(position = action.position) }

            is MainConfigActions.SetDecorationDraftTimeout -> updateDraft {
                it.copy(timeout = action.timeout, timeoutError = false)
            }

            is MainConfigActions.SetDecorationDraftMessage -> updateDraft {
                it.copy(message = action.message, messageError = false)
            }

            is MainConfigActions.SetDecorationDraftAmPm -> updateDraft { it.copy(ampm = action.ampm) }
            is MainConfigActions.SetDecorationDraftShowDate -> updateDraft { it.copy(showDate = action.showDate) }
            is MainConfigActions.SetDecorationDraftTimeFormat -> updateDraft { it.copy(timeFormat = action.timeFormat) }
            is MainConfigActions.SetDecorationDraftDateFormat -> updateDraft { it.copy(dateFormat = action.dateFormat) }

            MainConfigActions.AddDecoration -> addDecoration()
            is MainConfigActions.RemoveDecoration -> removeDecoration(action.index)

            MainConfigActions.SaveSettings -> saveSettings()
            MainConfigActions.ChangeServer -> changeServer()
        }
    }

    private fun updateDraft(transform: (DecorationDraftState) -> DecorationDraftState) {
        _state.update { it.copy(decorationDraft = transform(it.decorationDraft)) }
    }

    private fun addDecoration() {
        val draft = state.value.decorationDraft
        val timeout = draft.timeout.toIntOrNull()
        val isMessageMissing = draft.kind == DecorationDraftState.DecorationKind.MESSAGE && draft.message.isBlank()

        if (timeout == null || isMessageMissing) {
            updateDraft { it.copy(timeoutError = timeout == null, messageError = isMessageMissing) }
            return
        }

        _state.update {
            it.copy(
                decorations = it.decorations + draft.toServerConfigDecoration(timeout),
                decorationDraft = DecorationDraftState.DEFAULT.copy(position = draft.position)
            )
        }
    }

    private fun removeDecoration(index: Int) {
        _state.update { it.copy(decorations = it.decorations.filterIndexed { i, _ -> i != index }) }
    }

    private fun saveSettings() {
        val currentState = state.value
        val mediaItemTime = currentState.mediaItemTime.toIntOrNull()
        val sleepTimerFrom = currentState.sleepTimerFrom.toIntOrNull()
        val sleepTimerTo = currentState.sleepTimerTo.toIntOrNull()

        if (mediaItemTime == null || sleepTimerFrom == null || sleepTimerTo == null) {
            _state.update {
                it.copy(
                    mediaItemTimeError = mediaItemTime == null,
                    sleepTimerFromError = sleepTimerFrom == null,
                    sleepTimerToError = sleepTimerTo == null
                )
            }
            return
        }

        val serverConfig = currentState.toServerConfig(
            mediaItemTime = mediaItemTime,
            sleepTimerFrom = sleepTimerFrom,
            sleepTimerTo = sleepTimerTo
        )

        // TODO: persist/send serverConfig once a ServerConfig repository is defined
    }

    private fun changeServer() {
        clearConnectionConfigUseCase()
        viewModelScope.launch {
            _uiEventBus.emit(MainConfigUiEvents.NavigateToConnectionSetup)
        }
    }

}

private fun MainConfigState.toServerConfig(
    mediaItemTime: Int,
    sleepTimerFrom: Int,
    sleepTimerTo: Int
) = ServerConfig(
    decorations = decorations,
    mainUI = ServerConfigMainUI(
        hideType = mainUIHideType,
        hideTimeout = mainUIHideTimeout
    ),
    mediaItemTime = mediaItemTime,
    reshuffleAfterPlaylistFinish = reshuffleAfterPlaylistFinish,
    sleepTimerFrom = sleepTimerFrom,
    sleepTimerTo = sleepTimerTo,
    imageScaling = imageScaling,
    videoScaling = videoScaling,
    sortType = sortType,
    dirSortType = dirSortType
)

private fun DecorationDraftState.toServerConfigDecoration(timeout: Int): ServerConfigDecoration = when (kind) {
    DecorationDraftState.DecorationKind.CURRENT_DATE_TIME -> ServerConfigDecoration.CurrentDateTime(
        position = position,
        timeout = timeout,
        ampm = ampm,
        showDate = showDate,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )

    DecorationDraftState.DecorationKind.MESSAGE -> ServerConfigDecoration.Message(
        position = position,
        timeout = timeout,
        message = message
    )

    DecorationDraftState.DecorationKind.MEDIA_INFO -> ServerConfigDecoration.MediaInfo(
        position = position,
        timeout = timeout,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )
}

package nacholab.frame.server.ui

import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.ui.mainconfig.MainConfigMapper.buildFrom
import nacholab.frame.ui.model.LoadingState
import nacholab.frame.server.domain.repository.MediaItemRepository
import nacholab.frame.server.domain.repository.SettingsRepository
import nacholab.frame.server.ui.ServerAppActions.*
import nacholab.frame.server.domain.usecase.RequestDirToUserUseCase
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ServerAppViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val mediaItemRepository: MediaItemRepository,
): ViewModel(){

    private val _state = MutableStateFlow(ServerAppState.DEFAULT)
    val state = _state.asStateFlow()

    private val _uiEventBus = MutableSharedFlow<ServerAppUIEvents>(extraBufferCapacity = 1)
    val uiEventBus: SharedFlow<ServerAppUIEvents> = _uiEventBus.asSharedFlow()

    fun onAction(action: ServerAppActions){
        when (action){
            is LoadMedia -> loadMedia(action.activity)
            is SetBrightness -> _state.update { it.copy(brightness = action.brightness) }
            is SetMuted -> _state.update { it.copy(isMuted = action.isMuted) }
            is SetPlaying -> _state.update { it.copy(isPlaying = action.isPlaying) }
            is SetVideoPosition -> _state.update { it.copy(videoPosition = action.position) }
            is SetVolume -> _state.update { it.copy(volume = action.volume) }
            ToggleMuted -> onAction(SetMuted(!state.value.isMuted))
            TogglePlaying -> onAction(SetPlaying(!state.value.isPlaying))
            Sleep -> setSleepMode(true)
            Wakeup -> setSleepMode(false)
            StartMinuteClock -> startMinuteClock()
            is ReceiveServerConfig -> updateServerConfig(action.config)
            is SetServerPort -> _state.update { it.copy(currentPort = action.port) }
            is SetServerHost -> _state.update { it.copy(currentHost = action.host) }
        }
    }

    private fun updateServerConfig(serverConfig: ServerConfig){
        _state.update {
            it.copy(
                sleepFrom = serverConfig.sleepTimerFrom,
                sleepTo = serverConfig.sleepTimerTo,
                decorations = serverConfig.decorations.map { d -> buildFrom(d) },
                mainUIHideType = serverConfig.mainUI.hideType,
                mainUIHideTimeout = serverConfig.mainUI.hideTimeout,
                imageTimeout = serverConfig.mediaItemTime,
                rebuildMediaLibraryAfterPlaylistFinish = serverConfig.reshuffleAfterPlaylistFinish,
                imageScaling = buildFrom(serverConfig.imageScaling),
                videoScaling = buildFrom(serverConfig.videoScaling)
            )
        }

        val currentSortType = state.value.currentSortingType
        val currentDirSortType = state.value.currentDirSortingType
        val newSortType = serverConfig.sortType
        val newDirSortType = serverConfig.dirSortType
        val shouldReload = currentSortType!=newSortType || currentDirSortType!=newDirSortType

        if (shouldReload){
            viewModelScope.launch(Dispatchers.IO){
                _uiEventBus.emit(ServerAppUIEvents.RequestReload)
            }
        }
    }

    private fun startMinuteClock(){
        viewModelScope.launch {
            while (true){
                val now = LocalTime.now()
                val currentMinute = now.hour * 60 + now.minute
                _state.update { it.copy(minuteClock = currentMinute) }
                delay(60_000L)
            }
        }
    }

    private fun setSleepMode(sleepMode: Boolean){
        val currentlySleeping = state.value.sleepMode

        if (sleepMode!=currentlySleeping) {
            _state.update { it.copy(sleepMode = sleepMode) }
            onAction(SetPlaying(!sleepMode))
        }
    }

    private fun loadMedia(activity: ComponentActivity){
        _state.update { it.copy(loadingState = LoadingState.Loading) }

        val currentDir = settingsRepository.getSavedFolderUri()
        val requestDirToUserUseCase = RequestDirToUserUseCase(activity)

        viewModelScope.launch {
            val rootDirDocument = if (currentDir==null){
                requestDirToUserUseCase.execute()
            }else{
                requestDirToUserUseCase.buildDocumentFileFromUri(currentDir.toUri())
            }

            withContext(Dispatchers.IO) {
                settingsRepository.saveFolderUri(rootDirDocument?.uri?.toString())
                rootDirDocument?.let {
                    val fileSort = state.value.currentSortingType
                    val dirSort = state.value.currentDirSortingType

                    if (dirSort == ServerConfig.ServerConfigSorting.IGNORE) {
                        mediaItemRepository.buildMediaGalleryItemsNoDirSorted(it, fileSort)
                    }else{
                        mediaItemRepository.buildMediaGalleryItemsDirSorted(it, dirSort, fileSort)
                    }
                }

                _state.update {
                    it.copy(
                        loadingState = LoadingState.Success,
                        currentGallery = mediaItemRepository.getCurrentMediaItems()
                    )
                }
            }
        }
    }
}
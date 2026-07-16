package nacholab.frame.server.ui

import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nacholab.frame.data.GalleryItem
import nacholab.frame.data.LoadingState
import nacholab.frame.data.MediaItemRepository
import nacholab.frame.data.SettingsRepository
import nacholab.frame.usecases.RequestDirToUserUseCase
import javax.inject.Inject
import kotlin.random.Random

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
            is ServerAppActions.LoadMedia -> loadMedia(action.activity)
            is ServerAppActions.SetBrightness -> _state.update { it.copy(brightness = action.brightness) }
            is ServerAppActions.SetMuted -> _state.update { it.copy(isMuted = action.isMuted) }
            is ServerAppActions.SetPlaying -> _state.update { it.copy(isPlaying = action.isPlaying) }
            is ServerAppActions.SetVideoPosition -> _state.update { it.copy(videoPosition = action.position) }
            is ServerAppActions.SetVolume -> _state.update { it.copy(volume = action.volume) }
            ServerAppActions.ToggleMuted -> onAction(ServerAppActions.SetMuted(!state.value.isMuted))
            ServerAppActions.TogglePlaying -> onAction(ServerAppActions.SetPlaying(!state.value.isPlaying))
            ServerAppActions.Sleep -> setSleepMode(true)
            ServerAppActions.Wakeup -> setSleepMode(false)
        }
    }

    private fun setSleepMode(sleepMode: Boolean){
        val currentlySleeping = state.value.sleepMode

        if (sleepMode!=currentlySleeping) {
            _state.update { it.copy(sleepMode = sleepMode) }
            onAction(ServerAppActions.SetPlaying(!sleepMode))
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

                rootDirDocument?.let { mediaItemRepository.buildMediaGalleryItems(it) }

                val videos = mediaItemRepository.getCurrentMediaItems()
                    .filterIsInstance<GalleryItem.GalleryItemVideo>()
                val images = mediaItemRepository.getCurrentMediaItems()
                    .filterIsInstance<GalleryItem.GalleryItemImage>()

//                val mediaItems = ArrayList<GalleryItem>()
//                    .apply {
//                        add(videos.random())
//                        ArrayList<GalleryItem>().apply {
//                            addAll(videos)
//                            addAll(images)
//                        }
//                            .toList()
//                            .sortedBy { Random.nextInt() }
//                            .let { addAll(it) }
//                    }

                val mediaItems = ArrayList<GalleryItem>()
                    .apply {
                        addAll(videos)
                        addAll(images)
                    }
                    .toList()
                    .sortedBy { Random.nextInt() }

                _state.update {
                    it.copy(
                        loadingState = LoadingState.Success,
                        currentGallery = mediaItems
                    )
                }
            }
        }
    }

}
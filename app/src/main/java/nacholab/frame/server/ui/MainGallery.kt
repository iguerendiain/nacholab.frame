package nacholab.frame.server.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.media3.common.C
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nacholab.frame.R
import nacholab.frame.data.GalleryItem
import nacholab.frame.data.MainGalleryDecoration
import nacholab.frame.server.ui.composables.MainGalleryDecorations
import nacholab.frame.server.ui.composables.MainGalleryPager
import nacholab.frame.server.ui.composables.MainGalleryUI
import nacholab.frame.server.ui.composables.NextPageAfterTimeout
import nacholab.frame.server.ui.composables.SleepModeEffect
import nacholab.frame.server.ui.composables.VideoPlaybackControlEffect
import nacholab.frame.utils.BrightnessUtils
import nacholab.frame.utils.buildPlayer
import java.time.LocalTime

@Composable
fun MainGallery(
    mediaList: List<GalleryItem>,
    imageTimeout: Int,
    currentBrightness: Float,
    isPlaying: Boolean,
    isMuted: Boolean,
    currentVolume: Float,
    sleepMode: Boolean,
    sleepFrom: Int?,
    sleepTo: Int?,
    ampm: Boolean,
    decorations: List<MainGalleryDecoration>,
    onAction: (ServerAppActions) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { mediaList.size })
    val exoPlayer = buildPlayer()
    val context = LocalContext.current
    var currentMinute by remember { mutableIntStateOf(0) }

    var currentVideoPosition by remember { mutableLongStateOf(0L) }
    var currentVideoDuration by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            currentMinute = now.hour * 60 + now.minute
            delay(60_000L)
        }
    }

    SleepModeEffect(sleepFrom, sleepTo) {
        onAction(if (it) ServerAppActions.Sleep else ServerAppActions.Wakeup)
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            currentVideoPosition = exoPlayer.currentPosition
            currentVideoDuration = exoPlayer.duration.takeIf { it != C.TIME_UNSET } ?: 0L
            delay(100)
        }
    }

    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
    }

    LaunchedEffect(currentVolume, isMuted) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            maxVolume,
            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
        )

        exoPlayer.volume = if (isMuted) 0f else currentVolume
    }

    LaunchedEffect(sleepMode) {
        BrightnessUtils.setScreenBrightness(context, if (sleepMode) 0f else currentBrightness)
    }

    VideoPlaybackControlEffect(
        pagerState = pagerState,
        exoPlayer = exoPlayer,
        shouldPlay = isPlaying,
        mediaList = mediaList
    )

    NextPageAfterTimeout(
        pagerState = pagerState,
        mediaList = mediaList,
        timeout = imageTimeout * 1000L,
        enabled = isPlaying
    )

    var isOverlayVisible by remember { mutableStateOf(false) }
    var hideJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun showOverlayAndResetTimer() {
        isOverlayVisible = true
        hideJob?.cancel()
        hideJob = coroutineScope.launch {
            delay(5000)
            isOverlayVisible = false
        }
    }



    val currentPage = pagerState.settledPage
    val mediaItem = mediaList[currentPage]
    val isVideo = mediaItem is GalleryItem.GalleryItemVideo
    val isVisible = isVideo && !pagerState.isScrollInProgress
    if (isVisible)
    ContentFrame(
        player = exoPlayer,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
        surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
    )

    MainGalleryPager(
        pagerState = pagerState,
        mediaList = mediaList,
        exoPlayer = exoPlayer,
    ) {
        showOverlayAndResetTimer()
    }

    AnimatedVisibility(
        visible = isOverlayVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        val currentItem = mediaList[pagerState.currentPage]
        val title = currentItem.let {
            val name = if (it is GalleryItem.GalleryItemUri){
                it.uri.pathSegments.last().split("/").last()
            }else ""

            val type = when (it){
                is GalleryItem.GalleryItemImage -> stringResource(R.string.server_ui_title_photo)
                is GalleryItem.GalleryItemVideo -> stringResource(R.string.server_ui_title_video)
            }

            val currentItem = pagerState.settledPage + 1

            val totalCount = mediaList.size

            stringResource(R.string.server_ui_title).format(name, type, currentItem, totalCount)
        }

        val coroutineScope2 = rememberCoroutineScope()
        MainGalleryUI(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .6f))
                .clickable { isOverlayVisible = false },
            title = title,
            isPlaying = isPlaying,
            isVideo = currentItem is GalleryItem.GalleryItemVideo,
            isMuted = isMuted,
            currentVolume = currentVolume,
            currentBrightness = currentBrightness,
            currentVideoPosition = currentVideoPosition,
            currentVideoDuration = currentVideoDuration,
            sleepFrom = sleepFrom,
            sleepTo = sleepTo,
            currentTimeMinutes = currentMinute,
            ampm = ampm,

            togglePlayback = {
                showOverlayAndResetTimer()
                onAction(ServerAppActions.TogglePlaying)
            },
            scrubVideo = {
                showOverlayAndResetTimer()
                onAction(ServerAppActions.SetVideoPosition(it))
            },
            toggleMute = {
                showOverlayAndResetTimer()
                onAction(ServerAppActions.ToggleMuted)
            },
            changeVolume = {
                showOverlayAndResetTimer()
                onAction(ServerAppActions.SetVolume(it))
            },
            changeBrightness = {
                showOverlayAndResetTimer()
                onAction(ServerAppActions.SetBrightness(it))
            },
            sleep = { onAction(ServerAppActions.Sleep) },
            previousPage = {
                showOverlayAndResetTimer()
                coroutineScope2.launch {
                    val previousPage = (pagerState.settledPage - 1) % mediaList.size
                    pagerState.animateScrollToPage(previousPage)
                }
            },
            nextPage = {
                showOverlayAndResetTimer()
                coroutineScope2.launch {
                    val nextPage = (pagerState.settledPage + 1) % mediaList.size
                    pagerState.animateScrollToPage(nextPage)
                }
            },
        )
    }


    AnimatedVisibility(
        visible = !isOverlayVisible && decorations.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) { MainGalleryDecorations(decorations, mediaItem) }

    if (sleepMode){
        Box(modifier = Modifier.fillMaxSize().background(Color.Black).clickable{ onAction(ServerAppActions.Wakeup)})
    }
}

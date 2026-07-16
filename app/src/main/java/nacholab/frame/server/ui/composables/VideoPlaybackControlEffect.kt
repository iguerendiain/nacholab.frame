package nacholab.frame.server.ui.composables

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nacholab.frame.data.GalleryItem

@Composable
fun VideoPlaybackControlEffect(
    pagerState: PagerState,
    exoPlayer: ExoPlayer,
    shouldPlay: Boolean,
    mediaList: List<GalleryItem>
){
    val currentPage = pagerState.settledPage
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentPage, pagerState.isScrollInProgress) {
//        withContext(Dispatchers.Main) {
            if (pagerState.isScrollInProgress) {
                Log.d("COSO", "STOP")
                exoPlayer.stop()
                exoPlayer.clearMediaItems()
            } else {
                val mediaItem = mediaList.getOrNull(currentPage)
                if (mediaItem is GalleryItem.GalleryItemVideo) {
                    val file = mediaItem.uri.path?.split("/")?.last()
                    Log.d("COSO", "PLAY - $shouldPlay - $file ")
                    val mediaItem = MediaItem.fromUri(mediaItem.uri)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.seekTo(0)
                    if (shouldPlay) exoPlayer.play()

                exoPlayer.addListener(
                    object: Player.Listener{
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_ENDED) {
                                exoPlayer.removeListener(this)
                                coroutineScope.launch {
                                    val nextPage = (pagerState.settledPage + 1) % mediaList.size
                                    pagerState.animateScrollToPage(nextPage)
                                }
                            }
                        }
                    }
                )
                } else {
                    Log.d("COSO", "DO NOTHING")
                }
            }
//        }
    }

}
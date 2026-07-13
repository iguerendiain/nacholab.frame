package nacholab.frame.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import nacholab.frame.data.GalleryItem

@Composable
fun DebugStatus(
    item: GalleryItem,
    currentItem: Int,
    totalItems: Int,
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier
){
    val name = if (item is GalleryItem.GalleryItemUri) item.uri.path?.split("/")?.last() else ""
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var bufferedPosition by remember { mutableLongStateOf(0L) }
    var filename by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(false) }
    var playState by remember { mutableStateOf("") }

    LaunchedEffect(exoPlayer) {
        while (true) {
            playState = when (exoPlayer.playbackState){
                ExoPlayer.STATE_IDLE -> "Idle"
                ExoPlayer.STATE_BUFFERING -> "Buffering"
                ExoPlayer.STATE_READY -> "Ready"
                ExoPlayer.STATE_ENDED -> "Ended"
                else -> "Unknown"
            }
            isPlaying = exoPlayer.isPlaying
            filename = exoPlayer.currentMediaItem?.localConfiguration?.uri?.path?.split("/")?.last()?:""
            currentPosition = exoPlayer.currentPosition
            duration = exoPlayer.duration.takeIf { it != C.TIME_UNSET } ?: 0L
            bufferedPosition = exoPlayer.bufferedPosition
            delay(1000/30)
        }
    }

    Box(
        modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = .25f))
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.background(Color.Black.copy(alpha = .25f))
                ){
                    Text(
                        text = name?:"Unknown",
                        color = Color.White,
                        fontSize = 34.sp
                    )
                    Text(
                        text = when (item){
                            is GalleryItem.GalleryItemImage -> "[ Photo ]"
                            is GalleryItem.GalleryItemVideo -> "[ Video ]"
                        },
                        color = Color.White,
                        fontSize = 34.sp
                    )
                    Text(
                        text = "$currentItem / $totalItems",
                        color = Color.White,
                        fontSize = 34.sp
                    )
                }
            }

            if (item is GalleryItem.GalleryItemVideo){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = .25f))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(36.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(Color.Black.copy(alpha = .5f))
                    ) {


                        Text(
                            text = filename,
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Text(
                            text = if (isPlaying) "PLAYING" else "NOT PLAYING",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "State: $playState",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "$currentPosition / $duration -- ($bufferedPosition)",
                            color = Color.White,
                            fontSize = 34.sp
                        )
                    }
                }
            }
        }
    }
}
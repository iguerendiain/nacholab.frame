package nacholab.frame.server.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import nacholab.frame.ui.utils.buildPlayer

@Composable
fun GalleryItemVideoComposable(
    exoPlayer: ExoPlayer,
    isVisible: Boolean,
){
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Cyan).padding(20.dp),
        contentAlignment = Alignment.Center
    ){
//        ContentFrame(
//            player = exoPlayer,
//            modifier = Modifier.fillMaxSize().background(Color.Red).alpha(if (isVisible) 1f else 0f),
//            contentScale = ContentScale.Fit,
//            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
//        )

        if (!isVisible) CircularProgressIndicator(modifier = Modifier.size(72.dp), color = White)
    }
}

@Preview
@Composable
private fun GalleryItemVideoComposablePreview() {
    GalleryItemVideoComposable(
        exoPlayer = buildPlayer(),
        isVisible = false
    )
}
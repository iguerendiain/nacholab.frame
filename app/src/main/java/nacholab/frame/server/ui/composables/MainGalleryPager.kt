package nacholab.frame.server.ui.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import nacholab.frame.data.GalleryItem

@Composable
fun MainGalleryPager(
    pagerState: PagerState,
    mediaList: List<GalleryItem>,
    exoPlayer: ExoPlayer,
    onTap: () -> Unit
){
    val currentPage = pagerState.settledPage
    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Main)
                    if (up != null) onTap()
                }
            }
    ) { page ->
        mediaList[page].let { item ->
            when (item){
                is GalleryItem.GalleryItemImage -> GalleryItemImageComposable(item.uri)
                is GalleryItem.GalleryItemVideo -> {
                    if (pagerState.isScrollInProgress) Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(72.dp), color = White)
                    }

//                    GalleryItemVideoComposable(
//                        exoPlayer = exoPlayer,
//                        isVisible = page == currentPage && !pagerState.isScrollInProgress
//                    )
                }
            }
        }
    }
}
package nacholab.frame.ui.server.composables

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import nacholab.frame.data.GalleryItem

@Composable
fun NextPageAfterTimeout(
    pagerState: PagerState,
    mediaList: List<GalleryItem>,
    timeout: Long,
    enabled: Boolean
){
    val currentPage = pagerState.settledPage

    LaunchedEffect(currentPage, pagerState, enabled) {
        if (!enabled) return@LaunchedEffect

        snapshotFlow { pagerState.isScrollInProgress }
            .collect { scrolling ->
                if (scrolling) return@collect

                val mediaItem = mediaList.getOrNull(currentPage) ?: return@collect
                val isVideo = mediaItem is GalleryItem.GalleryItemVideo

                if (!isVideo) {
                    delay(timeout)
                    if (!pagerState.isScrollInProgress && enabled) {
                        val nextPage = (currentPage + 1) % mediaList.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }
    }
}
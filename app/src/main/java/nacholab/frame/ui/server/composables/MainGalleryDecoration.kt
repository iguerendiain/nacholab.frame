package nacholab.frame.ui.server.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import nacholab.frame.data.GalleryItem
import nacholab.frame.data.MainGalleryDecoration
import nacholab.frame.ui.server.composables.decorations.MainGalleryDecorationCurrentTime
import nacholab.frame.ui.server.composables.decorations.MainGalleryDecorationMediaInfo
import nacholab.frame.ui.server.composables.decorations.MainGalleryDecorationMessage
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@Composable
fun MainGalleryDecoration(decoration: MainGalleryDecoration, currentMediaItem: GalleryItem){
    when (decoration){
        is MainGalleryDecoration.CurrentTime -> MainGalleryDecorationCurrentTime(
            currentTime = SimpleDateFormat(decoration.timeFormat).format(System.currentTimeMillis()),
            currentDate = if (decoration.showDate) SimpleDateFormat(decoration.dateFormat).format(
                System.currentTimeMillis()
            ) else null
        )
        is MainGalleryDecoration.MediaInfo -> MainGalleryDecorationMediaInfo(
            mediaItem = currentMediaItem
        )
        is MainGalleryDecoration.Message -> MainGalleryDecorationMessage(
            decoration.message
        )
    }
}
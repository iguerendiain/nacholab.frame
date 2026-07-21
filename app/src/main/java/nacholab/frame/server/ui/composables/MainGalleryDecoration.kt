package nacholab.frame.server.ui.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import nacholab.frame.data.GalleryItem
import nacholab.frame.data.MainGalleryDecoration
import nacholab.frame.server.ui.composables.decorations.MainGalleryDecorationCurrentTime
import nacholab.frame.server.ui.composables.decorations.MainGalleryDecorationMediaInfo
import nacholab.frame.server.ui.composables.decorations.MainGalleryDecorationMessage
import nacholab.frame.utils.TimeFormatter
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@Composable
fun MainGalleryDecoration(
    decoration: MainGalleryDecoration,
    currentMediaItem: GalleryItem,
    currentMinute: Int,
){
    when (decoration){
        is MainGalleryDecoration.CurrentTime -> MainGalleryDecorationCurrentTime(
            currentTime = TimeFormatter.minutesToTime(currentMinute, decoration.ampm),
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
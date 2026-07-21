package nacholab.frame.server.ui.composables

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
private fun MainGalleryDecorationPreview() {
    Box(modifier = Modifier.background(Color.Gray)) {
        MainGalleryDecoration(
            decoration = MainGalleryDecoration.Message(
                position = MainGalleryDecoration.Position.BOTTOM_CENTER,
                timeout = null,
                message = "Welcome home!"
            ),
            currentMediaItem = GalleryItem.GalleryItemImage(
                uri = Uri.parse("content://sample/zaraza.jpg"),
                isRemote = false
            ),
            currentMinute = 934
        )
    }
}
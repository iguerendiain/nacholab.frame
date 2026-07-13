package nacholab.frame.ui.server.composables.decorations

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import nacholab.frame.data.GalleryItem
import nacholab.frame.utils.MetadataToolsTools
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@Composable
fun MainGalleryDecorationMediaInfo(
    mediaItem: GalleryItem,
){
    val fileName = if (mediaItem is GalleryItem.GalleryItemUri){
        mediaItem.uri.path?.split("/")?.last()
    }else ""

    val type: String
    val data: MetadataToolsTools.FileMetaData?

    when (mediaItem){
        is GalleryItem.GalleryItemImage -> {
            type = "Foto"
            data = MetadataToolsTools.extractImageExifData(mediaItem.uri)
        }
        is GalleryItem.GalleryItemVideo -> {
            type = "Video"
            data = MetadataToolsTools.extractVideoMetadata(mediaItem.uri)
        }
    }

    val localDescription = if (fileName!=null) {
        "$fileName ($type)"
    }else null

    val firstLine = data?.description?:localDescription

    val lines = ArrayList<String>()

    if (data?.description!=null && localDescription!=null) lines.add(localDescription)

    data
        ?.date
        ?.let { SimpleDateFormat("dd/MM/yyyy").format(it) }
        ?.let { lines.add(it) }

    data
        ?.camera
        ?.let { lines.add(it) }

    data
        ?.location
        ?.let { lines.add("${it.lat}, ${it.lng}")}

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        firstLine?.let { Text(
            text = it,
            color = Color.White,
            fontSize = 32.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(6f, 6f),
                    blurRadius = 4f
                )
            )
        ) }
        lines.forEach {
            Text(
                text = it,
                color = Color.White,
                fontSize = 24.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(6f, 6f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}
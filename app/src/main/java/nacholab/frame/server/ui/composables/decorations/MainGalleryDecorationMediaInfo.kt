package nacholab.frame.server.ui.composables.decorations

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import nacholab.frame.server.domain.model.GalleryItem
import nacholab.frame.utils.MetadataTools
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
    val data: MetadataTools.FileMetaData?

    when (mediaItem){
        is GalleryItem.GalleryItemImage -> {
            type = "Foto"
            data = MetadataTools.extractImageExifData(mediaItem.uri)
        }
        is GalleryItem.GalleryItemVideo -> {
            type = "Video"
            data = MetadataTools.extractVideoMetadata(mediaItem.uri)
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

@Preview
@Composable
private fun MainGalleryDecorationMediaInfoPreview() {
    Box(modifier = Modifier.background(Color.Gray)) {
        MainGalleryDecorationMediaInfo(
            mediaItem = GalleryItem.GalleryItemImage(
                uri = Uri.parse("content://sample/zaraza.jpg"),
                isRemote = false
            )
        )
    }
}
package nacholab.frame.server.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nacholab.frame.data.GalleryItem
import nacholab.frame.data.MainGalleryDecoration

@Composable
fun MainGalleryDecorations(
    decorations: List<MainGalleryDecoration>,
    currentMediaItem: GalleryItem,
    currentMinute: Int,
){
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(modifier = Modifier.fillMaxSize().weight(1f)){
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.TopStart
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.TOP_START }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.TopCenter
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.TOP_CENTER }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.TopEnd
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.TOP_END }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
        }
        Row(modifier = Modifier.fillMaxSize().weight(1f)){
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.CenterStart
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.MIDDLE_START }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.Center
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.MIDDLE_CENTER }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.CenterEnd
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.MIDDLE_END }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
        }
        Row(modifier = Modifier.fillMaxSize().weight(1f)){
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.BottomStart
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.BOTTOM_START }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.BottomCenter
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.BOTTOM_CENTER }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentAlignment = Alignment.BottomEnd
            ){
                decorations
                    .filter { it.position == MainGalleryDecoration.Position.BOTTOM_END }
                    .forEach { MainGalleryDecoration(it, currentMediaItem, currentMinute) }
            }
        }
    }
}

//@Preview(
//    showSystemUi = false,
//    device = "spec:width=480dp,height=800dp,dpi=420,orientation=landscape"
//)
//@Composable
//fun MainGalleryDecorationsPreview(){
//    NacholabFrameServerTheme {
//        Box(modifier = Modifier.fillMaxSize().background(Gray)){
//            MainGalleryDecorations(ServerAppState.DEFAULT.decorations, Media)
//        }
//    }
//}
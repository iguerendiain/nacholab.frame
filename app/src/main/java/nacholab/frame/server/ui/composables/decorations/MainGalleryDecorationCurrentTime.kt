package nacholab.frame.server.ui.composables.decorations

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

@Composable
fun MainGalleryDecorationCurrentTime(
    currentTime: String,
    currentDate: String?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 48.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(6f, 6f),
                    blurRadius = 4f
                )
            )
        )
        currentDate?.let { Text(
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
        )}
    }
}

@Preview
@Composable
fun MainGalleryDecorationCurrentTimePreview() {
    Box(modifier = Modifier.background(Color.Gray)) {
        MainGalleryDecorationCurrentTime(
            currentTime = "5:33PM",
            currentDate = "Lunes 4 de Noviembre de 2014"
        )
    }
}
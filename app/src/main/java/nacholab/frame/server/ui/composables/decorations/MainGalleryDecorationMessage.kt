package nacholab.frame.server.ui.composables.decorations

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MainGalleryDecorationMessage(message: String){
    Text(
        text = message,
        color = Color.White,
        fontSize = 72.sp,
        style = TextStyle(
            textAlign = TextAlign.Center,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.5f),
                offset = Offset(6f, 6f),
                blurRadius = 4f
            )
        )
    )
}
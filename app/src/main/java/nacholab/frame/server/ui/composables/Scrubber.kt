package nacholab.frame.server.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Scrubber(
    modifier: Modifier = Modifier,
    position: Float,
    onValueChange: (Float) -> Unit,
    trackColor: Color = Color.LightGray,
    progressColor: Color = Color.White,
    thumbColor: Color = Color.White,
) {
    var currentValue by remember(position) { mutableFloatStateOf(position.coerceIn(0f, 1f)) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDragCancel = {},
                    onDragEnd = {},
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newValue = (currentValue + dragAmount.x / size.width).coerceIn(0f, 1f)
                        currentValue = newValue
                        onValueChange(newValue)
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val trackHeight = 4.dp

        drawLine(
            color = trackColor,
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = trackHeight.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = progressColor,
            start = Offset(0f, centerY),
            end = Offset(width * currentValue, centerY),
            strokeWidth = trackHeight.toPx(),
            cap = StrokeCap.Round
        )

        val thumbCenterX = width * currentValue
        drawCircle(
            color = thumbColor,
            radius = size.height,
            center = Offset(thumbCenterX, centerY)
        )
    }
}

@Preview(widthDp = 240, heightDp = 24)
@Composable
private fun ScrubberPreview() {
    Box(modifier = Modifier.background(Color.Black)) {
        Scrubber(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            position = 0.4f,
            onValueChange = {}
        )
    }
}
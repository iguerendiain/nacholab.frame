package nacholab.frame.server.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import nacholab.frame.utils.ConnectionUri

@Composable
fun RemoteInfo(
    modifier: Modifier = Modifier,
    currentHost: String,
    currentPort: Int
){
    val connectionData = ConnectionUri.encode(currentHost, currentPort)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = .6f))
            .padding(20.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = connectionData,
                color = White,
                fontSize = 24.sp,
                modifier = Modifier
            )

            Image(
                painter = rememberQrCodePainter(connectionData),
                contentDescription = "QR Code",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .aspectRatio(1f)
                    .padding(24.dp)
                    .weight(1f)
            )

        }

    }
}

@Composable
@Preview
fun RemoteInfoPreview(){
    MaterialTheme {
        RemoteInfo(
            modifier = Modifier.fillMaxSize(),
            currentHost = "localhost",
            currentPort = 22
        )
    }
}

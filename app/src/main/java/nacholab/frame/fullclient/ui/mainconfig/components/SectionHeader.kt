package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import nacholab.frame.theme.NacholabFrameTheme

@Composable
fun SectionHeader(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge)
}

@Preview(showBackground = true)
@Composable
private fun SectionHeaderPreview() {
    NacholabFrameTheme {
        SectionHeader(title = "Playback")
    }
}

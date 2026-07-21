package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SectionHeader(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge)
}

package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.ui.mainconfig.displayName
import nacholab.frame.theme.NacholabFrameTheme

private val CapsuleShape = RoundedCornerShape(percent = 50)
private val CapsuleContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

@Composable
fun <T> CapsuleSelector(
    label: String,
    selected: T,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                if (option == selected) {
                    Button(
                        onClick = { onSelected(option) },
                        shape = CapsuleShape,
                        contentPadding = CapsuleContentPadding,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = optionLabel(option), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                } else {
                    OutlinedButton(
                        onClick = { onSelected(option) },
                        shape = CapsuleShape,
                        contentPadding = CapsuleContentPadding,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = optionLabel(option), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CapsuleSelectorPreview() {
    NacholabFrameTheme {
        CapsuleSelector(
            label = "Scaling",
            selected = ServerConfig.ServerConfigScaling.CROP,
            options = ServerConfig.ServerConfigScaling.entries,
            optionLabel = { it.displayName() },
            onSelected = {}
        )
    }
}

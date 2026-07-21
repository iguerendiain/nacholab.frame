package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions

@Composable
fun ActionButtonsSection(onAction: (MainConfigActions) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = { onAction(MainConfigActions.SaveSettings) },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save settings")
        }

        OutlinedButton(
            onClick = { onAction(MainConfigActions.ChangeServer) },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change server")
        }
    }
}

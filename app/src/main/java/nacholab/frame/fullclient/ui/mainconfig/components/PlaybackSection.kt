package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName
import nacholab.frame.theme.NacholabFrameTheme

@Composable
fun PlaybackSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader("Playback")

        

        OutlinedTextField(
            value = state.mediaItemTime,
            onValueChange = { onAction(MainConfigActions.SetMediaItemTime(it)) },
            label = { Text("Seconds per media item") },
            isError = state.mediaItemTimeError,
            supportingText = {
                if (state.mediaItemTimeError) Text("Enter a whole number of seconds")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Reshuffle after playlist finishes",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = state.reshuffleAfterPlaylistFinish,
                onCheckedChange = { onAction(MainConfigActions.SetReshuffleAfterPlaylistFinish(it)) }
            )
        }

        EnumDropdownField(
            label = "Image scaling",
            selected = state.imageScaling,
            options = ServerConfig.ServerConfigScaling.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetImageScaling(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdownField(
            label = "Video scaling",
            selected = state.videoScaling,
            options = ServerConfig.ServerConfigScaling.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetVideoScaling(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdownField(
            label = "File sort order",
            selected = state.sortType,
            options = ServerConfig.ServerConfigSorting.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetSortType(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdownField(
            label = "Directory sort order",
            selected = state.dirSortType,
            options = ServerConfig.ServerConfigSorting.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetDirSortType(it)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaybackSectionPreview() {
    NacholabFrameTheme {
        PlaybackSection(
            state = MainConfigState.DEFAULT,
            onAction = {}
        )
    }
}

package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName

@Composable
fun SortingSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    SectionHeader("Sorting")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Sort by directories",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = state.directorySortEnabled,
            onCheckedChange = { onAction(MainConfigActions.SetDirectorySortingEnabled(it)) }
        )
    }

    if (state.directorySortEnabled) EnumDropdownField(
        label = "Directories sort order",
        selected = state.dirSortType,
        options = ServerConfig.ServerConfigSorting.entries.filter { it != ServerConfig.ServerConfigSorting.IGNORE },
        optionLabel = { it.displayName() },
        onSelected = { onAction(MainConfigActions.SetDirSortType(it)) },
        modifier = Modifier.fillMaxWidth()
    )

    EnumDropdownField(
        label = "Files sort order",
        selected = state.sortType,
        options = ServerConfig.ServerConfigSorting.entries.filter { it != ServerConfig.ServerConfigSorting.IGNORE },
        optionLabel = { it.displayName() },
        onSelected = { onAction(MainConfigActions.SetSortType(it)) },
        modifier = Modifier.fillMaxWidth()
    )

    if (state.sortType == ServerConfig.ServerConfigSorting.RANDOM || state.dirSortType == ServerConfig.ServerConfigSorting.RANDOM)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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

}
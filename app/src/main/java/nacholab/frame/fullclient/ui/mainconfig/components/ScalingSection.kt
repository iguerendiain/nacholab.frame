package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName

@Composable
fun ScalingSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    SectionHeader("Scaling")

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
}
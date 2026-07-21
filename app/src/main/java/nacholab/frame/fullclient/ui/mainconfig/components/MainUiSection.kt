package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.domain.model.ServerConfigMainUI
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName
import nacholab.frame.theme.NacholabFrameTheme

@Composable
fun MainUiSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader("Main UI")

        OutlinedTextField(
            value = state.mainUIHideType,
            onValueChange = { onAction(MainConfigActions.SetMainUIHideType(it)) },
            label = { Text("Hide type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdownField(
            label = "Hide behavior",
            selected = state.mainUIHideTimeout,
            options = ServerConfigMainUI.ServerConfigMainUIHideType.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetMainUIHideTimeout(it)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainUiSectionPreview() {
    NacholabFrameTheme {
        MainUiSection(
            state = MainConfigState.DEFAULT,
            onAction = {}
        )
    }
}

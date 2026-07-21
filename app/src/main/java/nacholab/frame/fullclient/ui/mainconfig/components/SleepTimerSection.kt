package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.theme.NacholabFrameTheme

@Composable
fun SleepTimerSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader("Sleep timer")

        OutlinedTextField(
            value = state.sleepTimerFrom,
            onValueChange = { onAction(MainConfigActions.SetSleepTimerFrom(it)) },
            label = { Text("Sleep from (hour, 0-23)") },
            isError = state.sleepTimerFromError,
            supportingText = {
                if (state.sleepTimerFromError) Text("Enter a whole number")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.sleepTimerTo,
            onValueChange = { onAction(MainConfigActions.SetSleepTimerTo(it)) },
            label = { Text("Sleep until (hour, 0-23)") },
            isError = state.sleepTimerToError,
            supportingText = {
                if (state.sleepTimerToError) Text("Enter a whole number")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SleepTimerSectionPreview() {
    NacholabFrameTheme {
        SleepTimerSection(
            state = MainConfigState.DEFAULT,
            onAction = {}
        )
    }
}

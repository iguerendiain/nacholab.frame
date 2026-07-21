package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Use 12-hour clock (AM/PM)",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = state.sleepTimerAmPm,
                onCheckedChange = { onAction(MainConfigActions.SetSleepTimerAmPm(it)) }
            )
        }

        Text(text = "From", style = MaterialTheme.typography.titleMedium)

        TimeOfDayPicker(
            hour = state.sleepTimerFromHour,
            minute = state.sleepTimerFromMinute,
            use24HourFormat = !state.sleepTimerAmPm,
            onHourChange = { onAction(MainConfigActions.SetSleepTimerFromHour(it)) },
            onMinuteChange = { onAction(MainConfigActions.SetSleepTimerFromMinute(it)) }
        )

        Text(text = "To", style = MaterialTheme.typography.titleMedium)

        TimeOfDayPicker(
            hour = state.sleepTimerToHour,
            minute = state.sleepTimerToMinute,
            use24HourFormat = !state.sleepTimerAmPm,
            onHourChange = { onAction(MainConfigActions.SetSleepTimerToHour(it)) },
            onMinuteChange = { onAction(MainConfigActions.SetSleepTimerToMinute(it)) }
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

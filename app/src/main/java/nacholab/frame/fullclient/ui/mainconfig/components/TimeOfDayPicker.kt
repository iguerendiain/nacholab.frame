package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nacholab.frame.theme.NacholabFrameTheme

private const val HOURS_PER_DAY = 24
private const val HOURS_PER_HALF_DAY = 12
private const val MINUTES_PER_HOUR = 60

@Composable
fun TimeOfDayPicker(
    hour: Int,
    minute: Int,
    use24HourFormat: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (use24HourFormat) {
            EnumDropdownField(
                label = "Hour",
                selected = hour,
                options = (0 until HOURS_PER_DAY).toList(),
                optionLabel = { "%02d".format(it) },
                onSelected = onHourChange,
                modifier = Modifier.weight(1f)
            )
        } else {
            EnumDropdownField(
                label = "Hour",
                selected = hour.to12Hour(),
                options = (1..HOURS_PER_HALF_DAY).toList(),
                optionLabel = { it.toString() },
                onSelected = { onHourChange(it.from12Hour(pm = hour.isPm())) },
                modifier = Modifier.weight(1f)
            )
        }

        EnumDropdownField(
            label = "Minute",
            selected = minute,
            options = (0 until MINUTES_PER_HOUR).toList(),
            optionLabel = { "%02d".format(it) },
            onSelected = onMinuteChange,
            modifier = Modifier.weight(1f)
        )

        if (!use24HourFormat) {
            EnumDropdownField(
                label = "AM/PM",
                selected = hour.isPm(),
                options = listOf(false, true),
                optionLabel = { if (it) "PM" else "AM" },
                onSelected = { pm -> onHourChange(hour.to12Hour().from12Hour(pm)) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun Int.isPm(): Boolean = this >= HOURS_PER_HALF_DAY

private fun Int.to12Hour(): Int = when {
    this == 0 -> HOURS_PER_HALF_DAY
    this > HOURS_PER_HALF_DAY -> this - HOURS_PER_HALF_DAY
    else -> this
}

private fun Int.from12Hour(pm: Boolean): Int {
    val hour0to11 = this % HOURS_PER_HALF_DAY
    return if (pm) hour0to11 + HOURS_PER_HALF_DAY else hour0to11
}

@Preview(showBackground = true)
@Composable
private fun TimeOfDayPicker24HourPreview() {
    NacholabFrameTheme {
        TimeOfDayPicker(
            hour = 22,
            minute = 30,
            use24HourFormat = true,
            onHourChange = {},
            onMinuteChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeOfDayPickerAmPmPreview() {
    NacholabFrameTheme {
        TimeOfDayPicker(
            hour = 22,
            minute = 30,
            use24HourFormat = false,
            onHourChange = {},
            onMinuteChange = {}
        )
    }
}

package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.fullclient.ui.mainconfig.DecorationDraftState
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName
import nacholab.frame.fullclient.ui.mainconfig.summary

fun LazyListScope.decorationsSection(
    state: MainConfigState,
    onAction: (MainConfigActions) -> Unit
) {
    item { SectionHeader("Decorations") }

    if (state.decorations.isEmpty()) {
        item {
            Text(
                text = "No decorations added yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    itemsIndexed(state.decorations) { index, decoration ->
        DecorationRow(
            decoration = decoration,
            onRemove = { onAction(MainConfigActions.RemoveDecoration(index)) }
        )
    }

    item {
        DecorationDraftForm(
            draft = state.decorationDraft,
            onAction = onAction
        )
    }
}

@Composable
private fun DecorationRow(
    decoration: ServerConfigDecoration,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = decoration.summary(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove decoration")
        }
    }
}

@Composable
private fun DecorationDraftForm(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        HorizontalDivider()

        Text(text = "Add decoration", style = MaterialTheme.typography.bodyLarge)

        EnumDropdownField(
            label = "Type",
            selected = draft.kind,
            options = DecorationDraftState.DecorationKind.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetDecorationDraftKind(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdownField(
            label = "Position",
            selected = draft.position,
            options = ServerConfigDecoration.ServerConfigDecorationPosition.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetDecorationDraftPosition(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.timeout,
            onValueChange = { onAction(MainConfigActions.SetDecorationDraftTimeout(it)) },
            label = { Text("Timeout (seconds)") },
            isError = draft.timeoutError,
            supportingText = {
                if (draft.timeoutError) Text("Enter a whole number of seconds")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        when (draft.kind) {
            DecorationDraftState.DecorationKind.CURRENT_DATE_TIME -> CurrentDateTimeDraftFields(draft, onAction)
            DecorationDraftState.DecorationKind.MESSAGE -> MessageDraftFields(draft, onAction)
            DecorationDraftState.DecorationKind.MEDIA_INFO -> MediaInfoDraftFields(draft, onAction)
        }

        OutlinedButton(
            onClick = { onAction(MainConfigActions.AddDecoration) },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add decoration")
        }
    }
}

@Composable
private fun CurrentDateTimeDraftFields(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Use AM/PM", style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = draft.ampm,
            onCheckedChange = { onAction(MainConfigActions.SetDecorationDraftAmPm(it)) }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Show date", style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = draft.showDate,
            onCheckedChange = { onAction(MainConfigActions.SetDecorationDraftShowDate(it)) }
        )
    }

    OutlinedTextField(
        value = draft.timeFormat,
        onValueChange = { onAction(MainConfigActions.SetDecorationDraftTimeFormat(it)) },
        label = { Text("Time format") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = draft.dateFormat,
        onValueChange = { onAction(MainConfigActions.SetDecorationDraftDateFormat(it)) },
        label = { Text("Date format") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MessageDraftFields(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit
) {
    OutlinedTextField(
        value = draft.message,
        onValueChange = { onAction(MainConfigActions.SetDecorationDraftMessage(it)) },
        label = { Text("Message") },
        isError = draft.messageError,
        supportingText = {
            if (draft.messageError) Text("Enter a message")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MediaInfoDraftFields(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit
) {
    OutlinedTextField(
        value = draft.timeFormat,
        onValueChange = { onAction(MainConfigActions.SetDecorationDraftTimeFormat(it)) },
        label = { Text("Time format") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = draft.dateFormat,
        onValueChange = { onAction(MainConfigActions.SetDecorationDraftDateFormat(it)) },
        label = { Text("Date format") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

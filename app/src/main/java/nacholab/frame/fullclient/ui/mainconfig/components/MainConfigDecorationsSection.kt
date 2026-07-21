package nacholab.frame.fullclient.ui.mainconfig.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.fullclient.ui.mainconfig.DecorationDraftState
import nacholab.frame.fullclient.ui.mainconfig.MainConfigActions
import nacholab.frame.fullclient.ui.mainconfig.MainConfigState
import nacholab.frame.fullclient.ui.mainconfig.displayName
import nacholab.frame.fullclient.ui.mainconfig.summary
import nacholab.frame.theme.NacholabFrameTheme

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
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tap a position to add a decoration",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            DecorationPositionGrid(
                onPositionSelected = { onAction(MainConfigActions.OpenAddDecorationSheet(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (state.isAddDecorationSheetVisible) {
        item {
            AddDecorationBottomSheet(
                draft = state.decorationDraft,
                onAction = onAction,
                onDismissRequest = { onAction(MainConfigActions.DismissAddDecorationSheet) }
            )
        }
    }
}

@Composable
private fun DecorationPositionGrid(
    onPositionSelected: (ServerConfigDecoration.ServerConfigDecorationPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ServerConfigDecoration.ServerConfigDecorationPosition.entries.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { position ->
                    OutlinedButton(
                        onClick = { onPositionSelected(position) },
                        shape = MaterialTheme.shapes.small,
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        Text(
                            text = position.displayName(),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DecorationPositionGridPreview() {
    NacholabFrameTheme {
        DecorationPositionGrid(onPositionSelected = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDecorationBottomSheet(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = SheetState(
            initialValue = SheetValue.Expanded,
            skipPartiallyExpanded = true,
            density = LocalDensity.current,
        )
    ) {
        DecorationDraftForm(
            draft = draft,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddDecorationBottomSheetPreview() {
    NacholabFrameTheme {
        AddDecorationBottomSheet(
            draft = DecorationDraftState.DEFAULT,
            onAction = {},
            onDismissRequest = {}
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

@Preview(showBackground = true)
@Composable
private fun DecorationRowPreview() {
    NacholabFrameTheme {
        DecorationRow(
            decoration = ServerConfigDecoration.Message(
                position = ServerConfigDecoration.ServerConfigDecorationPosition.TC,
                timeout = 5,
                message = "Welcome home!"
            ),
            onRemove = {}
        )
    }
}

@Composable
private fun DecorationDraftForm(
    draft: DecorationDraftState,
    onAction: (MainConfigActions) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(text = "Add decoration", style = MaterialTheme.typography.titleLarge)

        CapsuleSelector(
            label = "Type",
            selected = draft.kind,
            options = DecorationDraftState.DecorationKind.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetDecorationDraftKind(it)) },
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

        EnumDropdownField(
            label = "Position",
            selected = draft.position,
            options = ServerConfigDecoration.ServerConfigDecorationPosition.entries,
            optionLabel = { it.displayName() },
            onSelected = { onAction(MainConfigActions.SetDecorationDraftPosition(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onAction(MainConfigActions.AddDecoration) },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add decoration")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DecorationDraftFormPreview() {
    NacholabFrameTheme {
        DecorationDraftForm(
            draft = DecorationDraftState.DEFAULT,
            onAction = {}
        )
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

@Preview(showBackground = true)
@Composable
private fun CurrentDateTimeDraftFieldsPreview() {
    NacholabFrameTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CurrentDateTimeDraftFields(
                draft = DecorationDraftState.DEFAULT.copy(kind = DecorationDraftState.DecorationKind.CURRENT_DATE_TIME),
                onAction = {}
            )
        }
    }
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

@Preview(showBackground = true)
@Composable
private fun MessageDraftFieldsPreview() {
    NacholabFrameTheme {
        MessageDraftFields(
            draft = DecorationDraftState.DEFAULT.copy(
                kind = DecorationDraftState.DecorationKind.MESSAGE,
                message = "Welcome home!"
            ),
            onAction = {}
        )
    }
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

@Preview(showBackground = true)
@Composable
private fun MediaInfoDraftFieldsPreview() {
    NacholabFrameTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MediaInfoDraftFields(
                draft = DecorationDraftState.DEFAULT.copy(kind = DecorationDraftState.DecorationKind.MEDIA_INFO),
                onAction = {}
            )
        }
    }
}

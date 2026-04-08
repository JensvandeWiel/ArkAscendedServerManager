@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.drop
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.Checkbox
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle
import org.jetbrains.jewel.ui.typography

enum class LabelPosition {
    Inline,
    Above,
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
) {
    val state = remember { TextFieldState(value) }
    val currentValue = rememberUpdatedState(value)

    LaunchedEffect(value) {
        if (state.text.toString() != value) {
            state.edit {
                replace(0, length, value)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .drop(1)
            .collect { nextValue ->
                if (nextValue != currentValue.value) {
                    onValueChange(nextValue)
                }
            }
    }

    FormTextField(state, label, modifier, hint, error, readOnly, enabled, labelPosition)
}

@Composable
fun FormTextField(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
) {
    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    TextField(
                        state,
                        Modifier.fillMaxWidth(),
                        outline = if (error) Outline.Error else Outline.None,
                        readOnly = readOnly,
                        enabled = enabled,
                    )
                }
            }

            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    TextField(
                        state,
                        Modifier.fillMaxWidth(),
                        outline = if (error) Outline.Error else Outline.None,
                        readOnly = readOnly,
                        enabled = enabled
                    )
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            })
            {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormCheckboxField(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    hint: String? = null,
    error: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val fullContent = @Composable {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                outline = if (error) Outline.Error else Outline.None
            )
            Text(label, style = JewelTheme.typography.labelTextStyle)
        }
    }


    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun CheckboxSectionHeader(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
) {
    val style = LocalGroupHeaderStyle.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormCheckboxField(
            checked = checked,
            onCheckedChange = onCheckedChange,
            label = label,
            hint = hint,
            error = error,
        )

        Divider(
            orientation = Orientation.Horizontal,
            modifier = Modifier.weight(1f),
            color = style.colors.divider,
            thickness = style.metrics.dividerThickness,
            startIndent = style.metrics.indent,
        )
    }
}


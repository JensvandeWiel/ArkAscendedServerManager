@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.drop
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.typography

@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String? = null,
    error: Boolean = false,
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

    FormField(state, label, hint, error)
}

@Composable
fun FormField(state: TextFieldState, label: String, hint: String? = null, error: Boolean = false) {
    val fullContent = @Composable {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = JewelTheme.typography.labelTextStyle)
            TextField(
                state,
                Modifier.fillMaxWidth(),
                outline = if (error) Outline.Error else Outline.None,
            )
        }
    }

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


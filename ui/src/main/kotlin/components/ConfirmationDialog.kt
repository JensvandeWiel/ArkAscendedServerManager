package eu.wynq.arkascendedservermanager.ui.components

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.action_cancel
import arkascendedservermanager.ui.generated.resources.action_confirm
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedDialog
import io.github.kdroidfilter.nucleus.window.jewel.JewelDialogTitleBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    message: String,
) {
    val confirmLabel = stringResource(Res.string.action_confirm)
    val cancelLabel = stringResource(Res.string.action_cancel)
    val startupDialogState = rememberDialogState(size = DpSize(460.dp, 150.dp))
    JewelDecoratedDialog(
        onCloseRequest = onDismiss,
        title = title,
        resizable = false,
        state = startupDialogState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JewelTheme.globalColors.panelBackground),
        ) {
            JewelDialogTitleBar {
                Text(title)
            }
            Row(Modifier.fillMaxWidth().padding(16.dp)) {
                Text(message)
            }
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick = onConfirm) {
                    Text(confirmLabel)
                }
                DefaultButton(onClick = onDismiss) {
                    Text(cancelLabel)
                }
            }
        }
    }
}
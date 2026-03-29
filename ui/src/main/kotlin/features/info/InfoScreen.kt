package eu.wynq.arkascendedservermanager.ui.features.info

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.page_info
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InfoScreen(component: InfoComponent) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(Res.string.page_info))
        Text("Toast playground")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DefaultButton(onClick = component::showSampleInfoToast) {
                Text("Show timed toast")
            }
            DefaultButton(onClick = component::showPersistentErrorToast) {
                Text("Show persistent toast")
            }
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::clearToasts) {
                Text("Clear all")
            }
        }
    }
}
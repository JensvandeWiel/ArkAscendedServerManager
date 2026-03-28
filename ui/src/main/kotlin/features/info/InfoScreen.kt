package eu.wynq.arkascendedservermanager.ui.features.info

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.page_info
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InfoScreen(component: InfoComponent) {
    Text(stringResource(Res.string.page_info))
}
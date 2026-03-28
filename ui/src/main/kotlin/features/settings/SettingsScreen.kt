package eu.wynq.arkascendedservermanager.ui.features.settings

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.page_settings
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.Text

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Text(stringResource(Res.string.page_settings))
}
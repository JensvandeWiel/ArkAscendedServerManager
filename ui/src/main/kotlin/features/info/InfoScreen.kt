package eu.wynq.arkascendedservermanager.ui.features.info

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.action_clear_all
import arkascendedservermanager.ui.generated.resources.action_show_persistent_toast
import arkascendedservermanager.ui.generated.resources.action_show_timed_toast
import arkascendedservermanager.ui.generated.resources.info_toast_action_a
import arkascendedservermanager.ui.generated.resources.info_toast_action_b
import arkascendedservermanager.ui.generated.resources.info_toast_persistent_error_text
import arkascendedservermanager.ui.generated.resources.info_toast_persistent_error_title
import arkascendedservermanager.ui.generated.resources.info_toast_playground_title
import arkascendedservermanager.ui.generated.resources.info_toast_sample_text
import arkascendedservermanager.ui.generated.resources.info_toast_sample_title
import arkascendedservermanager.ui.generated.resources.info_toast_settings_content_description
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
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InfoScreen(component: InfoComponent) {
    val pageInfo = stringResource(Res.string.page_info)
    val toastPlaygroundTitle = stringResource(Res.string.info_toast_playground_title)
    val timedToastLabel = stringResource(Res.string.action_show_timed_toast)
    val persistentToastLabel = stringResource(Res.string.action_show_persistent_toast)
    val clearAllLabel = stringResource(Res.string.action_clear_all)

    val sampleToastTitle = stringResource(Res.string.info_toast_sample_title)
    val sampleToastText = stringResource(Res.string.info_toast_sample_text)
    val sampleToastActionA = stringResource(Res.string.info_toast_action_a)
    val sampleToastActionB = stringResource(Res.string.info_toast_action_b)
    val sampleToastSettingsDescription = stringResource(Res.string.info_toast_settings_content_description)

    val persistentErrorTitle = stringResource(Res.string.info_toast_persistent_error_title)
    val persistentErrorText = stringResource(Res.string.info_toast_persistent_error_text)
    val appVersion = AppBuildInfo.version

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(pageInfo)
        Text("Version: $appVersion")
        Text(toastPlaygroundTitle)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DefaultButton(
                onClick = {
                    component.showSampleInfoToast(
                        title = sampleToastTitle,
                        text = sampleToastText,
                        actionALabel = sampleToastActionA,
                        actionBLabel = sampleToastActionB,
                        settingsContentDescription = sampleToastSettingsDescription,
                    )
                }
            ) {
                Text(timedToastLabel)
            }
            DefaultButton(onClick = { component.showPersistentErrorToast(persistentErrorTitle, persistentErrorText) }) {
                Text(persistentToastLabel)
            }
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::clearToasts) {
                Text(clearAllLabel)
            }
        }
    }
}
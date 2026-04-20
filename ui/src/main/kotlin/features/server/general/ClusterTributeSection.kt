package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.ini.ServerSettings
import eu.wynq.arkascendedservermanager.core.server.defaultValueInt
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormOptionalSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun ClusterTributeSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val clusterTributeOptionsGroupLabel = stringResource(Res.string.server_details_group_cluster_tribute_options)
    val noTransferFromFilteringLabel = stringResource(Res.string.server_details_no_transfer_from_filtering_label)
    val noTransferFromFilteringHint = stringResource(Res.string.server_details_no_transfer_from_filtering_hint)
    val maxTributeDinoUploadsLabel = stringResource(Res.string.server_details_max_tribute_dino_uploads_label)
    val maxTributeDinoUploadsHint = stringResource(Res.string.server_details_max_tribute_dino_uploads_hint)
    val maxTributeItemUploadsLabel = stringResource(Res.string.server_details_max_tribute_item_uploads_label)
    val maxTributeItemUploadsHint = stringResource(Res.string.server_details_max_tribute_item_uploads_hint)
    val overrideSurvivorUploadExpirationLabel =
        stringResource(Res.string.server_details_override_survivor_upload_expiration_label)
    val overrideSurvivorUploadExpirationHint =
        stringResource(Res.string.server_details_override_survivor_upload_expiration_hint)
    val overrideDinoUploadExpirationLabel =
        stringResource(Res.string.server_details_override_dino_upload_expiration_label)
    val overrideDinoUploadExpirationHint =
        stringResource(Res.string.server_details_override_dino_upload_expiration_hint)
    val overrideItemUploadExpirationLabel =
        stringResource(Res.string.server_details_override_item_upload_expiration_label)
    val overrideItemUploadExpirationHint =
        stringResource(Res.string.server_details_override_item_upload_expiration_hint)

    model.server?.run {
        GroupHeader(clusterTributeOptionsGroupLabel)
        FormCheckboxField(
            settings.options.noTransferFromFiltering,
            onCheckedChange = { newValue ->
                component.updateServerOptions {
                    it.copy(noTransferFromFiltering = newValue)
                }
            },
            label = noTransferFromFilteringLabel,
            hint = noTransferFromFilteringHint,
        )
        FormSliderField(
            gameUserSettings.serverSettings.maxTributeDinos,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(maxTributeDinos = newValue))
                }
            },
            label = maxTributeDinoUploadsLabel,
            valueRange = 20..100,
            hint = maxTributeDinoUploadsHint,
            allowOutsideRange = false,
            showManualInput = true,
        )
        FormSliderField(
            gameUserSettings.serverSettings.maxTributeItems,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(maxTributeItems = newValue))
                }
            },
            label = maxTributeItemUploadsLabel,
            valueRange = 50..100,
            hint = maxTributeItemUploadsHint,
            allowOutsideRange = false,
            showManualInput = true,
        )
        FormOptionalSliderField(
            value = gameUserSettings.serverSettings.tributeCharacterExpirationSeconds?.div(60),
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(
                        serverSettings = it.serverSettings.copy(
                            tributeCharacterExpirationSeconds = newValue?.times(60)
                        )
                    )
                }
            },
            defaultValue = ServerSettings::tributeCharacterExpirationSeconds.defaultValueInt!! / 60,
            label = overrideSurvivorUploadExpirationLabel,
            valueRange = 0..604_800 / 60,
            hint = overrideSurvivorUploadExpirationHint,
            allowOutsideRange = true,
            showManualInput = true,
        )
        FormOptionalSliderField(
            value = gameUserSettings.serverSettings.tributeDinoExpirationSeconds?.div(60),
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(
                        serverSettings = it.serverSettings.copy(
                            tributeDinoExpirationSeconds = newValue?.times(60)
                        )
                    )
                }
            },
            defaultValue = ServerSettings::tributeDinoExpirationSeconds.defaultValueInt!! / 60,
            hint = overrideDinoUploadExpirationHint,
            label = overrideDinoUploadExpirationLabel,
            valueRange = 0..604_800 / 60,
            allowOutsideRange = true,
            showManualInput = true,
        )
        FormOptionalSliderField(
            value = gameUserSettings.serverSettings.tributeItemExpirationSeconds?.div(60),
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(
                        serverSettings = it.serverSettings.copy(
                            tributeItemExpirationSeconds = newValue?.times(60)
                        )
                    )
                }
            },
            defaultValue = ServerSettings::tributeItemExpirationSeconds.defaultValueInt!! / 60,
            hint = overrideItemUploadExpirationHint,
            valueRange = 0..604_800 / 60,
            allowOutsideRange = true,
            showManualInput = true,
            label = overrideItemUploadExpirationLabel
        )
    }
}


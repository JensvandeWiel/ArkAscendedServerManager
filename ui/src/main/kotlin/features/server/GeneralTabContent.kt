@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.ini.ServerSettings
import eu.wynq.arkascendedservermanager.core.server.defaultValueInt
import eu.wynq.arkascendedservermanager.core.server.defaultValueString
import eu.wynq.arkascendedservermanager.ui.components.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun GeneralTabContent(component: ServerComponent) {
    val model by component.model.subscribeAsState()
    val clusters by component.clusters.collectAsState()
    val profileGroupLabel = stringResource(Res.string.server_details_group_profile)
    val clusterLabel = stringResource(Res.string.server_details_cluster_label)
    val nameAndPasswordsGroupLabel = stringResource(Res.string.server_details_group_name_and_passwords)
    val portsGroupLabel = stringResource(Res.string.server_details_group_ports)
    val mapAndModsGroupLabel = stringResource(Res.string.server_details_group_map_and_mods)
    val serverOptionsGroupLabel = stringResource(Res.string.server_details_group_server_options)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val serverNameLabel = stringResource(Res.string.server_details_server_name_label)
    val serverNameHint = stringResource(Res.string.server_details_server_name_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)
    val serverPasswordLabel = stringResource(Res.string.server_details_server_password_label)
    val serverPasswordHint = stringResource(Res.string.server_details_server_password_hint)
    val adminPasswordLabel = stringResource(Res.string.server_details_admin_password_label)
    val adminPasswordHint = stringResource(Res.string.server_details_admin_password_hint)
    val serverPortLabel = stringResource(Res.string.server_details_server_port_label)
    val serverPortHint = stringResource(Res.string.server_details_server_port_hint)
    val peerPortLabel = stringResource(Res.string.server_details_peer_port_label)
    val peerPortHint = stringResource(Res.string.server_details_peer_port_hint)
    val queryPortLabel = stringResource(Res.string.server_details_query_port_label)
    val queryPortHint = stringResource(Res.string.server_details_query_port_hint)
    val rconLabel = stringResource(Res.string.server_details_rcon_label)
    val rconHint = stringResource(Res.string.server_details_rcon_hint)
    val rconPortLabel = stringResource(Res.string.server_details_rcon_port_label)
    val rconPortHint = stringResource(Res.string.server_details_rcon_port_hint)
    val rconServerGameLogBufferLabel = stringResource(Res.string.server_details_rcon_server_game_log_buffer_label)
    val rconServerGameLogBufferHint = stringResource(Res.string.server_details_rcon_server_game_log_buffer_hint)
    val mapLabel = stringResource(Res.string.server_details_map_label)
    val mapHint = stringResource(Res.string.server_details_map_hint)
    val modsLabel = stringResource(Res.string.server_details_mods_label)
    val modsHint = stringResource(Res.string.server_details_mods_hint)
    val savesGroupLabel = stringResource(Res.string.server_details_group_saves)
    val autoSavePeriodLabel = stringResource(Res.string.server_details_auto_save_period_label)
    val autoSavePeriodHint = stringResource(Res.string.server_details_auto_save_period_hint)
    val motdGroupLabel = stringResource(Res.string.server_details_group_motd)
    val motdMessageLabel = stringResource(Res.string.server_details_motd_message_label)
    val motdMessageHint = stringResource(Res.string.server_details_motd_message_hint)
    val motdDurationLabel = stringResource(Res.string.server_details_motd_duration_label)
    val motdDurationHint = stringResource(Res.string.server_details_motd_duration_hint)
    val tributeDownloadOptionsGroupLabel = stringResource(Res.string.server_details_group_tribute_download_options)
    val enableTributeDownloadsLabel = stringResource(Res.string.server_details_enable_tribute_downloads_label)
    val enableTributeDownloadsHint = stringResource(Res.string.server_details_enable_tribute_downloads_hint)
    val noSurvivorDownloadsLabel = stringResource(Res.string.server_details_no_survivor_downloads_label)
    val noSurvivorDownloadsHint = stringResource(Res.string.server_details_no_survivor_downloads_hint)
    val noItemDownloadsLabel = stringResource(Res.string.server_details_no_item_downloads_label)
    val noItemDownloadsHint = stringResource(Res.string.server_details_no_item_downloads_hint)
    val noDinosaurDownloadsLabel = stringResource(Res.string.server_details_no_dinosaur_downloads_label)
    val noDinosaurDownloadsHint = stringResource(Res.string.server_details_no_dinosaur_downloads_hint)
    val foreignDinoDownloadsLabel = stringResource(Res.string.server_details_foreign_dino_downloads_label)
    val foreignDinoDownloadsHint = stringResource(Res.string.server_details_foreign_dino_downloads_hint)
    val tributeUploadOptionsGroupLabel = stringResource(Res.string.server_details_group_tribute_upload_options)
    val noSurvivorUploadsLabel = stringResource(Res.string.server_details_no_survivor_uploads_label)
    val noSurvivorUploadsHint = stringResource(Res.string.server_details_no_survivor_uploads_hint)
    val noItemUploadsLabel = stringResource(Res.string.server_details_no_item_uploads_label)
    val noItemUploadsHint = stringResource(Res.string.server_details_no_item_uploads_hint)
    val noDinosaurUploadsLabel = stringResource(Res.string.server_details_no_dinosaur_uploads_label)
    val noDinosaurUploadsHint = stringResource(Res.string.server_details_no_dinosaur_uploads_hint)
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
    val slotsLabel = stringResource(Res.string.server_details_slots_label)
    val slotsHint = stringResource(Res.string.server_details_slots_hint)
    val idlePlayerKickTimeHint = stringResource(Res.string.server_details_idle_player_kick_time_hint)
    val idlePlayerKickEnabledLabel = stringResource(Res.string.server_details_idle_player_kick_enabled_label)
    val idlePlayerKickEnabledHint = stringResource(Res.string.server_details_idle_player_kick_enabled_hint)
    val clusterIdLabel = stringResource(Res.string.server_details_cluster_id_label)
    val clusterIdHint = stringResource(Res.string.server_details_cluster_id_hint)
    val clusterDirOverrideLabel = stringResource(Res.string.server_details_cluster_dir_override_label)
    val clusterDirOverrideHint = stringResource(Res.string.server_details_cluster_dir_override_hint)
    val banListUrlLabel = stringResource(Res.string.server_details_ban_list_url_label)
    val banListUrlHint = stringResource(Res.string.server_details_ban_list_url_hint)
    val useDynamicConfigLabel = stringResource(Res.string.server_details_use_dynamic_config_label)
    val useDynamicConfigHint = stringResource(Res.string.server_details_use_dynamic_config_hint)
    val customDynamicConfigUrlHint = stringResource(Res.string.server_details_custom_dynamic_config_url_hint)
    val customLiveTuningUrlLabel = stringResource(Res.string.server_details_custom_live_tuning_url_label)
    val customLiveTuningUrlHint = stringResource(Res.string.server_details_custom_live_tuning_url_hint)

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(8.dp).padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(profileGroupLabel)
                FormTextField(
                    value = profileName,
                    onValueChange = { newValue ->
                        component.updateServer {
                            it.copy(profileName = newValue)
                        }
                    },
                    label = profileNameLabel,
                    error = !validateProfileName(),
                )
                FormTextField(
                    value = installationLocation,
                    onValueChange = { newValue ->
                        component.updateServer {
                            it.copy(installationLocation = newValue)
                        }
                    },
                    label = installationPathLabel,
                    hint = installationPathHint,
                    error = !validateInstallationLocation(),
                )
                FormCheckboxField(
                    asaApi,
                    onCheckedChange = { newValue ->
                        component.updateServer {
                            it.copy(asaApi = newValue)
                        }
                    },
                    label = asaApiLabel,
                    hint = asaApiHint,
                )
                GroupHeader(nameAndPasswordsGroupLabel)
                FormTextField(
                    value = gameUserSettings.sessionSettings.sessionName,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(sessionSettings = it.sessionSettings.copy(sessionName = newValue))
                        }
                    },
                    label = serverNameLabel,
                    hint = serverNameHint,
                    error = !gameUserSettings.sessionSettings.validateSessionName(),
                )
                FormTextField(
                    value = settings.administration.serverPassword ?: "",
                    onValueChange = { newValue ->
                        val normalizedPassword = newValue.trim().takeIf { it.isNotEmpty() }
                        component.updateServerAdministrationSettings {
                            it.copy(serverPassword = normalizedPassword)
                        }
                    },
                    label = serverPasswordLabel,
                    hint = serverPasswordHint,
                    error = !settings.administration.validateServerPassword(),
                )
                FormTextField(
                    value = settings.administration.adminPassword,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(adminPassword = newValue.trim())
                        }
                    },
                    label = adminPasswordLabel,
                    hint = adminPasswordHint,
                    error = !settings.administration.validateAdminPassword(),
                )
                GroupHeader(portsGroupLabel)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormTextField(
                        value = settings.administration.serverPort.toString(),
                        onValueChange = { newValue ->
                            val port = newValue.toIntOrNull()
                            if (port != null) {
                                component.updateServerAdministrationSettings {
                                    it.copy(serverPort = port)
                                }
                            }
                        },
                        label = serverPortLabel,
                        modifier = Modifier.weight(1f),
                        hint = serverPortHint,
                        error = !settings.administration.validateServerPort(),
                    )
                    FormTextField(
                        value = settings.administration.peerPort.toString(),
                        onValueChange = { /* Peer port is always server port +1, so it is not editable. */ },
                        label = peerPortLabel,
                        modifier = Modifier.weight(1f),
                        hint = peerPortHint,
                        error = !settings.administration.validatePeerPort(),
                        readOnly = true,
                    )
                    FormTextField(
                        value = settings.administration.queryPort.toString(),
                        onValueChange = { newValue ->
                            val port = newValue.toIntOrNull()
                            if (port != null) {
                                component.updateServerAdministrationSettings {
                                    it.copy(queryPort = port)
                                }
                            }
                        },
                        label = queryPortLabel,
                        modifier = Modifier.weight(1f),
                        hint = queryPortHint,
                        error = !settings.administration.validateQueryPort(),
                    )
                }
                CheckboxSectionHeader(
                    checked = settings.administration.rconEnabled,
                    onCheckedChange = { enabled ->
                        component.updateServerAdministrationSettings {
                            it.copy(rconEnabled = enabled)
                        }
                    },
                    label = rconLabel,
                    hint = rconHint,
                )
                FormTextField(
                    value = settings.administration.rconPort.toString(),
                    onValueChange = { newValue ->
                        val port = newValue.toIntOrNull()
                        if (port != null) {
                            component.updateServerAdministrationSettings {
                                it.copy(rconPort = port)
                            }
                        }
                    },
                    label = rconPortLabel,
                    hint = rconPortHint,
                    error = if (settings.administration.rconEnabled)
                        !settings.administration.validateRconPort() else false,
                    enabled = settings.administration.rconEnabled
                )
                FormTextField(
                    value = gameUserSettings.serverSettings.rconServerGameLogBuffer.toString(),
                    onValueChange = { newValue: String ->
                        val buffer = newValue.toIntOrNull()
                        if (buffer != null) {
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(rconServerGameLogBuffer = buffer))
                            }
                        }
                    },
                    label = rconServerGameLogBufferLabel,
                    hint = rconServerGameLogBufferHint,
                    error = !gameUserSettings.serverSettings.validateRconServerGameLogBuffer(),
                    enabled = settings.administration.rconEnabled
                )
                GroupHeader(mapAndModsGroupLabel)
                FormTextField(
                    value = settings.administration.map,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(map = newValue)
                        }
                    },
                    label = mapLabel,
                    hint = mapHint,
                    error = !settings.administration.validateMap(),
                )
                FormTextField(
                    value = settings.administration.mods.joinToString(","),
                    onValueChange = { newValue ->
                        val mods =
                            newValue.split(",").mapNotNull { rawValue -> rawValue.trim().takeIf { it.isNotEmpty() } }
                        component.updateServerAdministrationSettings {
                            it.copy(mods = mods)
                        }
                    },
                    label = modsLabel,
                    hint = modsHint,
                    error = !settings.administration.validateMods(),
                )
                GroupHeader(savesGroupLabel)
                FormSliderField(
                    value = gameUserSettings.serverSettings.autoSavePeriodMinutes,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(autoSavePeriodMinutes = newValue))
                        }
                    },
                    label = autoSavePeriodLabel,
                    valueRange = 0..240,
                    hint = autoSavePeriodHint,
                    allowOutsideRange = true,
                    showManualInput = true,
                    error = !gameUserSettings.serverSettings.validateAutoSavePeriodMinutes(),

                    )
                GroupHeader(motdGroupLabel)
                FormTextarea(
                    value = gameUserSettings.messageOfTheDay.message,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(messageOfTheDay = it.messageOfTheDay.copy(message = newValue))
                        }
                    },
                    label = motdMessageLabel,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    hint = motdMessageHint,
                    error = !gameUserSettings.messageOfTheDay.validate()
                )
                FormSliderField(
                    value = gameUserSettings.messageOfTheDay.duration,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(messageOfTheDay = it.messageOfTheDay.copy(duration = newValue))
                        }
                    },
                    label = motdDurationLabel,
                    valueRange = 0..120,
                    hint = motdDurationHint,
                    allowOutsideRange = true,
                    showManualInput = true,
                    error = !gameUserSettings.messageOfTheDay.validate(),
                )
                GroupHeader(stringResource(Res.string.clusters_configuration_group))
                FormSelectField(
                    value = cluster,
                    onValueChange = component::updateServerCluster,
                    options = clusters.values.toList(),
                    optionLabel = { clusterOption ->
                        clusterOption?.name ?: runBlocking { getString(Res.string.clusters_none) }
                    },
                    label = clusterLabel,
                    compareWith = { a, b -> a?.id == b?.id }
                )
                FormTextField(
                    value = settings.administration.clusterId ?: "",
                    onValueChange = {},
                    label = clusterIdLabel,
                    readOnly = true,
                    hint = clusterIdHint,
                )
                FormTextField(
                    value = settings.administration.clusterDirOverride ?: "",
                    onValueChange = {},
                    label = clusterDirOverrideLabel,
                    readOnly = true,
                    hint = clusterDirOverrideHint,
                )
                CheckboxSectionHeader(
                    !gameUserSettings.serverSettings.noTributeDownloads,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(noTributeDownloads = !newValue))
                        }
                    },
                    label = enableTributeDownloadsLabel,
                    hint = enableTributeDownloadsHint,
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventDownloadSurvivors,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventDownloadSurvivors = newValue))
                            }
                        },
                        label = noSurvivorDownloadsLabel,
                        hint = noSurvivorDownloadsHint,
                        enabled = !gameUserSettings.serverSettings.noTributeDownloads

                    )
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventDownloadItems,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventDownloadItems = newValue))
                            }
                        },
                        label = noItemDownloadsLabel,
                        hint = noItemDownloadsHint,
                        enabled = !gameUserSettings.serverSettings.noTributeDownloads
                    )
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventDownloadDinos,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventDownloadDinos = newValue))
                            }
                        },
                        label = noDinosaurDownloadsLabel,
                        hint = noDinosaurDownloadsHint,
                        enabled = !gameUserSettings.serverSettings.noTributeDownloads
                    )
                }
                FormCheckboxField(
                    gameUserSettings.serverSettings.crossArkAllowForeignDinoDownloads,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(crossArkAllowForeignDinoDownloads = newValue))
                        }
                    },
                    label = foreignDinoDownloadsLabel,
                    hint = foreignDinoDownloadsHint,
                    enabled = !gameUserSettings.serverSettings.noTributeDownloads
                )
                GroupHeader(tributeUploadOptionsGroupLabel)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventUploadSurvivors,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventUploadSurvivors = newValue))
                            }
                        },
                        label = noSurvivorUploadsLabel,
                        hint = noSurvivorUploadsHint,
                    )
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventUploadItems,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventUploadItems = newValue))
                            }
                        },
                        label = noItemUploadsLabel,
                        hint = noItemUploadsHint,
                    )
                    FormCheckboxField(
                        gameUserSettings.serverSettings.preventUploadDinos,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(preventUploadDinos = newValue))
                            }
                        },
                        label = noDinosaurUploadsLabel,
                        hint = noDinosaurUploadsHint,
                    )
                }
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
                                    tributeCharacterExpirationSeconds = newValue?.times(
                                        60
                                    )
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
                                    tributeDinoExpirationSeconds = newValue?.times(
                                        60
                                    )
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
                                    tributeItemExpirationSeconds = newValue?.times(
                                        60
                                    )
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
                GroupHeader(serverOptionsGroupLabel)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FormCheckboxField(
                        settings.options.enableIdlePlayerKick,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions {
                                it.copy(enableIdlePlayerKick = newValue)
                            }
                        },
                        label = idlePlayerKickEnabledLabel,
                        hint = idlePlayerKickEnabledHint,
                    )
                    FormSliderField(
                        value = gameUserSettings.serverSettings.kickIdlePlayersPeriod,
                        onValueChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(kickIdlePlayersPeriod = newValue))
                            }
                        },
                        label = null,
                        valueRange = 0..7200,
                        hint = idlePlayerKickTimeHint,
                        allowOutsideRange = true,
                        showManualInput = true,
                        enabled = settings.options.enableIdlePlayerKick,
                        error = if (settings.options.enableIdlePlayerKick) !gameUserSettings.serverSettings.validateKickIdlePlayersPeriod() else false,
                    )
                }
                FormOptionalTextField(
                    value = gameUserSettings.serverSettings.banListUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(banListUrl = newValue))
                        }
                    },
                    defaultValue = ServerSettings::banListUrl.defaultValueString!!,
                    label = banListUrlLabel,
                    hint = banListUrlHint,
                )
                FormToggleableNullableTextField(
                    value = gameUserSettings.serverSettings.customDynamicConfigUrl,
                    onValueChange = { newValue: String? ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(customDynamicConfigUrl = newValue))
                        }
                    },
                    enabled = settings.options.useDynamicConfig,
                    onEnabledChange = { newValue ->
                        component.updateServerOptions {
                            it.copy(useDynamicConfig = newValue)
                        }
                    },
                    label = useDynamicConfigLabel,
                    hint = useDynamicConfigHint,
                    fieldHint = customDynamicConfigUrlHint,
                    error = !gameUserSettings.serverSettings.validateCustomDynamicConfigUrl(),
                )
                FormOptionalTextField(
                    value = gameUserSettings.serverSettings.customLiveTuningUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(customLiveTuningUrl = newValue))
                        }
                    },
                    defaultValue = ServerSettings::customLiveTuningUrl.defaultValueString!!,
                    label = customLiveTuningUrlLabel,
                    hint = customLiveTuningUrlHint,
                )
                FormSliderField(
                    value = settings.administration.slots,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(slots = newValue)
                        }
                    },
                    label = slotsLabel,
                    valueRange = 1..250,
                    error = !settings.administration.validateSlots(),
                    hint = slotsHint,
                    allowOutsideRange = true,
                    showManualInput = true,
                )
            }
        }
    }
}
package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.*
import arkascendedservermanager.ui.generated.resources.*
import eu.wynq.arkascendedservermanager.core.ini.Game
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.components.FormTextarea
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import serialization.IniSerializer
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ConfigEditorTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    model.server?.run {
        val editedGameIni = remember(game) { mutableStateOf(IniSerializer.serialize(game)) }
        val editedGameUserSettingsIni = remember(gameUserSettings) { mutableStateOf(IniSerializer.serialize(gameUserSettings)) }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InlineWarningBanner {
                Text(stringResource(Res.string.config_editor_warning_text))
                Text(stringResource(Res.string.config_editor_warning_custom_fields))
            }
            Row(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                    FormTextarea(
                        value = editedGameIni.value,
                        onValueChange = { editedGameIni.value = it },
                        label = stringResource(Res.string.config_editor_game_ini_label),
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        hint = stringResource(Res.string.config_editor_game_ini_label)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                    FormTextarea(
                        value = editedGameUserSettingsIni.value,
                        onValueChange = { editedGameUserSettingsIni.value = it },
                        label = stringResource(Res.string.config_editor_game_user_settings_ini_label),
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        hint = stringResource(Res.string.config_editor_game_user_settings_ini_label)
                    )
                }
            }
            DefaultButton(onClick = {
                val gameResult = runCatching { IniSerializer.deserialize<Game>(editedGameIni.value) }
                val gameUserSettingsResult = runCatching { IniSerializer.deserialize<GameUserSettings>(editedGameUserSettingsIni.value) }

                val errors = mutableListOf<String>()
                if (gameResult.isFailure) {
                    errors.add("Game.ini: (${gameResult.exceptionOrNull()!!::class}) ${gameResult.exceptionOrNull()!!.message}")
                }
                if (gameUserSettingsResult.isFailure) {
                    errors.add("GameUserSettings.ini: (${gameUserSettingsResult.exceptionOrNull()!!::class}) ${gameUserSettingsResult.exceptionOrNull()!!.message}")
                }

                if (errors.isEmpty()) {
                    component.updateServerGame { gameResult.getOrThrow() }
                    component.updateServerGameUserSettings { gameUserSettingsResult.getOrThrow() }
                    ToastBannerManager.show(ToastBannerType.SUCCESS, "Configs applied successfully")
                } else {
                    ToastBannerManager.show(ToastBannerType.ERROR, "Invalid configs: ${errors.joinToString("; ")}")
                }
            }) {
                Text(stringResource(Res.string.config_editor_apply_changes))
            }
        }
    }
}

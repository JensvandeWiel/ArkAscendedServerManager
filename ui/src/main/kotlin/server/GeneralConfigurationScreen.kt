package ui.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.surface.Card
import ui.server.components.ModsManagerView
import ui.server.components.administration.GeneralConfigurationAutoSaveSection
import ui.server.components.administration.GeneralConfigurationBasicFields
import ui.server.components.administration.GeneralConfigurationMotdSection
import ui.server.components.administration.GeneralConfigurationPortsSection
import ui.server.components.administration.GeneralConfigurationRconSection

@Composable
fun GeneralConfigurationScreen(component: GeneralConfigurationComponent) {
    val administrationModel by component.generalConfigurationModel.subscribeAsState()
    val gameUserSettingsModel by component.gameUserSettingsModel.subscribeAsState()

    Card(
        Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            GeneralConfigurationBasicFields(component, administrationModel)
            GeneralConfigurationPortsSection(component, administrationModel)
            GeneralConfigurationRconSection(component, administrationModel)
            ModsManagerView(
                mods = administrationModel.mods,
                onModsChanged = { updatedMods ->
                    component.updateModsList(updatedMods)
                }
            )
            GeneralConfigurationAutoSaveSection(component, gameUserSettingsModel)
            GeneralConfigurationMotdSection(component, gameUserSettingsModel)
        }
    }
}
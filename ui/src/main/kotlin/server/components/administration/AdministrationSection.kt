package ui.server.components.administration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.Text
import components.CollapsibleCard
import ui.server.ServerComponent
import ui.server.components.ModsManagerView

@Composable
fun AdministrationSection(component: ServerComponent) {
    val administrationModel by component.administrationModel.subscribeAsState()
    val gameUserSettingsModel by component.gameUserSettingsModel.subscribeAsState()

    CollapsibleCard(
        title = { Text("Administration", style = FluentTheme.typography.bodyStrong) },
        initiallyExpanded = false
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AdministrationBasicFields(component, administrationModel)
            AdministrationPortsSection(component, administrationModel)
            AdministrationRconSection(component, administrationModel)
            ModsManagerView(
                mods = administrationModel.mods,
                onModsChanged = { updatedMods ->
                    component.updateModsList(updatedMods)
                }
            )
            AdministrationAutoSaveSection(component, gameUserSettingsModel)
        }
    }
}

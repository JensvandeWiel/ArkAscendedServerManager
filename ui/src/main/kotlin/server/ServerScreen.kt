package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composefluent.component.ListItemSeparator
import ui.server.components.InstallationSection
import ui.server.components.ProfileConfigurationSection
import ui.server.components.ServerControlSection
import ui.server.components.ServerHeaderSection
import ui.server.components.administration.AdministrationSection

@Composable
fun ServerScreen(component: ServerComponent) {
    val scrollState = rememberScrollState()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp, 0.dp)) {
            ServerHeaderSection(component)
            InstallationSection(component)
            ServerControlSection(component)
        }

        ListItemSeparator(Modifier)

        Column(Modifier.verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ProfileConfigurationSection(component)
            AdministrationSection(component)
        }
    }
}

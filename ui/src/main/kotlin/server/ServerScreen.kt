package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.component.Icon
import io.github.composefluent.component.SelectorBar
import io.github.composefluent.component.SelectorBarItem
import io.github.composefluent.component.Text
import io.github.composefluent.icons.Icons
import io.github.composefluent.icons.regular.Options
import io.github.composefluent.icons.regular.Wrench
import io.github.composefluent.surface.Card
import ui.server.components.InstallationSection
import ui.server.components.ServerControlSection
import ui.server.components.ServerHeaderSection

@Composable
fun ServerScreen(component: ServerComponent) {
    // Subscribe to reactive values
    val isProfileConfigurationActive by component.isProfileConfigurationActive.subscribeAsState()
    val isGeneralConfigActive by component.isGeneralConfigurationActive.subscribeAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Header section that's always visible
        Card(Modifier) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
            ) {
                ServerHeaderSection(component)
                InstallationSection(component)
                ServerControlSection(component)
                SelectorBar {
                    SelectorBarItem(
                        selected = isProfileConfigurationActive,
                        onSelectedChange = { component.navigateToProfileConfiguration() },
                        text = { Text("Profile") },
                        icon = { Icon(Icons.Regular.Options, contentDescription = "Profile") }
                    )

                    SelectorBarItem(
                        selected = isGeneralConfigActive,
                        onSelectedChange = { component.navigateToGeneralConfiguration() },
                        text = { Text("General") },
                        icon = { Icon(Icons.Regular.Wrench, contentDescription = "General") }
                    )
                }
            }
        }

        // Content area based on selected tab
        Children(
            stack = component.stack,
            modifier = Modifier.fillMaxSize()
        ) { child ->
            when (child.instance) {
                is ServerComponent.ServerChild.ProfileConfiguration -> {
                    ProfileConfigurationScreen(ProfileConfigurationComponent(
                        componentContext = component,
                        profileConfigurationModel = component.profileConfigurationModel,
                    ))
                }

                is ServerComponent.ServerChild.GeneralConfiguration -> {
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier.verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GeneralConfigurationScreen(
                            GeneralConfigurationComponent(
                                componentContext = component,
                                generalConfigurationModel = component.generalConfigurationModel,
                                gameUserSettingsModel = component.gameUserSettingsModel
                            )
                        )
                    }
                }
            }
        }
    }
}

package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import io.github.composefluent.surface.Card

@Composable
fun ProfileConfigurationScreen(component: ProfileConfigurationComponent) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val profileConfigurationModel by component.profileConfigurationModel.subscribeAsState()

        Card(
            Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                TextField(
                    value = profileConfigurationModel.profileName,
                    onValueChange = { newValue ->
                        component.updateProfileConfigurationModel(
                            component.profileConfigurationModel.value.copy(profileName = newValue)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    header = { Text("Profile Name") },
                )
                TextField(
                    value = profileConfigurationModel.installationLocation,
                    onValueChange = { newValue ->
                        component.updateProfileConfigurationModel(
                            component.profileConfigurationModel.value.copy(installationLocation = newValue)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    header = { Text("Installation Location") },
                )
            }
        }
    }
}

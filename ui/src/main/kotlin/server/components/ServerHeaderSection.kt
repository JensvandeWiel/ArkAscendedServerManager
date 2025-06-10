package ui.server.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.AccentButton
import com.konyaco.fluent.component.Text
import ui.server.ServerComponent

@Composable
fun ServerHeaderSection(component: ServerComponent) {
    val server by component.server.subscribeAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Profile: " + server.profileName, style = FluentTheme.typography.body)
        Spacer(modifier = Modifier.weight(1f))
        AccentButton({
            component.saveProfileConfiguration()
        }, modifier = Modifier.padding(start = 8.dp)) {
            Text("Save")
        }
    }
}

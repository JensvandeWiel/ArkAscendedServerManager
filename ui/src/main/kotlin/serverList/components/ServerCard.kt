package serverList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.surface.Card
import server.ServerProfile

@Composable
fun ServerCard(
    server: ServerProfile,
    onClick: () -> Unit = { }
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().height(128.dp))
    {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(
                text = server.profileName,
                style = FluentTheme.typography.subtitle
            )
        }
    }
}

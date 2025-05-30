package ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.component.TextField

@Composable
fun SettingsScreen(component: SettingsComponent) {
    // Subscribe to state changes from the component
    val model by component.model.subscribeAsState()

    Column {
        Text("Settings Screen is open")

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.value1,
            onValueChange = { newValue ->
                component.updateValue1(newValue)
            },
            placeholder = { Text("Enter settings value") }
        )
    }
}

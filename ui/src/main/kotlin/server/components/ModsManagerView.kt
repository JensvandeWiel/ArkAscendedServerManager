package ui.server.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.*
import com.konyaco.fluent.icons.Icons
import com.konyaco.fluent.icons.filled.Add
import com.konyaco.fluent.icons.filled.Delete

@Composable
fun ModsManagerView(
    mods: MutableList<Int>,
    onModsChanged: (MutableList<Int>) -> Unit
) {
    var modIdInput by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Mods",
            style = FluentTheme.typography.bodyStrong,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = modIdInput,
                onValueChange = {
                    // Only allow numbers
                    if (it.text.isEmpty() || it.text.all { char -> char.isDigit() }) {
                        modIdInput = it
                    }
                },
                placeholder = { Text("Enter mod ID")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    val modId = modIdInput.text.toIntOrNull()
                    if (modId != null && modId > 0 && !mods.contains(modId)) {
                        val updatedMods = mods.toMutableList().also { it.add(modId) }
                        onModsChanged(updatedMods)
                        modIdInput = TextFieldValue("")
                    }
                },
                disabled = !(modIdInput.text.isNotEmpty() && modIdInput.text.toIntOrNull() != null && modIdInput.text.toIntOrNull()!! > 0)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.padding(end = 4.dp))
                    Text("Add Mod")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(1.dp, RoundedCornerShape(4.dp))
        ) {
            if (mods.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No mods added yet")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(mods) { modId ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "Mod ID: $modId",
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                iconOnly = true,
                                onClick = {
                                    val updatedMods = mods.toMutableList().also { it.remove(modId) }
                                    onModsChanged(updatedMods)
                                }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete mod")
                            }
                        }
                        ListItemSeparator(Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

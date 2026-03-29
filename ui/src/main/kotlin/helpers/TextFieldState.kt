package eu.wynq.arkascendedservermanager.ui.helpers

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

fun TextFieldState.value(): String = text.toString()

class TextFieldStateWithError(
    text: String = "",
    error: Boolean = false,
) {
    val state = TextFieldState(text)
    var error by mutableStateOf(error)

    fun value(): String = state.value()
}

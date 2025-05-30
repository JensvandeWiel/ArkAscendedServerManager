package ui.settings

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class SettingsComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val _model = MutableValue(SettingsModel(TextFieldValue("")))
    val model: Value<SettingsModel> get() = _model

    init {
        _model.subscribe {
            println("SettingsModel updated: value1 = ${it.value1.text}")
        }
    }

    fun updateValue1(newValue: TextFieldValue) {
        println("Updating value1 to: ${newValue.text}")
        _model.value = _model.value.copy(value1 = newValue)
    }
}

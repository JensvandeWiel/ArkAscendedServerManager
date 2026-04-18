@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui.components

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.form_path_select_directory_title
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import eu.wynq.arkascendedservermanager.ui.LocalAppWindow
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.flow.drop
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle
import org.jetbrains.jewel.ui.component.styling.TextAreaStyle
import org.jetbrains.jewel.ui.theme.textAreaStyle
import org.jetbrains.jewel.ui.typography


enum class LabelPosition {
    Inline,
    Above,
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String?,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
) {
    val state = remember { TextFieldState(value) }
    val currentValue = rememberUpdatedState(value)

    LaunchedEffect(value) {
        if (state.text.toString() != value) {
            state.edit {
                replace(0, length, value)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .drop(1)
            .collect { nextValue ->
                if (nextValue != currentValue.value) {
                    onValueChange(nextValue)
                }
            }
    }

    FormTextField(state, label, modifier, hint, error, readOnly, enabled, labelPosition)
}

@Composable
fun FormOptionalTextField(
    value: String?,
    onValueChange: (String?) -> Unit,
    defaultValue: String,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
    readOnly: Boolean = false,
) {
    val isChecked = value != null
    val effectiveValue = value ?: defaultValue

    val fullContent = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { checked ->
                    if (checked) {
                        onValueChange(value ?: defaultValue)
                    } else {
                        onValueChange(null)
                    }
                },
                outline = if (error) Outline.Error else Outline.None,
                enabled = enabled,
            )

            FormTextField(
                value = effectiveValue,
                onValueChange = { nextValue -> onValueChange(nextValue) },
                label = label,
                modifier = Modifier.weight(1f),
                hint = null,
                error = error,
                enabled = enabled && isChecked,
                labelPosition = labelPosition,
                readOnly = readOnly,
            )
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormTextField(
    state: TextFieldState,
    label: String?,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
) {
    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (label != null) {
                        Text(label, style = JewelTheme.typography.labelTextStyle)
                    }
                    TextField(
                        state,
                        Modifier.fillMaxWidth(),
                        outline = if (error) Outline.Error else Outline.None,
                        readOnly = readOnly,
                        enabled = enabled,
                    )
                }
            }

            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (label != null) {
                        Text(label, style = JewelTheme.typography.labelTextStyle)
                    }
                    TextField(
                        state,
                        Modifier.fillMaxWidth(),
                        outline = if (error) Outline.Error else Outline.None,
                        readOnly = readOnly,
                        enabled = enabled
                    )
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            })
            {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormCheckboxField(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val fullContent = @Composable {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                outline = if (error) Outline.Error else Outline.None,
                enabled = enabled,
            )
            Text(label, style = JewelTheme.typography.labelTextStyle)
        }
    }


    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun CheckboxSectionHeader(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
) {
    val style = LocalGroupHeaderStyle.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormCheckboxField(
            checked = checked,
            onCheckedChange = onCheckedChange,
            label = label,
            hint = hint,
            error = error,
        )

        Divider(
            orientation = Orientation.Horizontal,
            modifier = Modifier.weight(1f),
            color = style.colors.divider,
            thickness = style.metrics.dividerThickness,
            startIndent = style.metrics.indent,
        )
    }
}

@Composable
fun FormSliderField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    valueRange: IntRange,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
    showManualInput: Boolean = false,
    allowOutsideRange: Boolean = false,
    steps: Int = 0,
) {
    require(!valueRange.isEmpty()) { "valueRange must not be empty" }

    val minValue = valueRange.first
    val maxValue = valueRange.last
    val clampedValue = value.coerceIn(minValue, maxValue)
    val manualValue = if (allowOutsideRange) value else clampedValue
    var isSliderDragging by remember { mutableStateOf(false) }
    val sliderValue = remember { mutableFloatStateOf(clampedValue.toFloat()) }

    val inputState = remember { TextFieldState(manualValue.toString()) }
    val currentValue = rememberUpdatedState(manualValue)
    val onValueChangeState = rememberUpdatedState(onValueChange)

    LaunchedEffect(clampedValue, isSliderDragging) {
        if (!isSliderDragging) {
            val targetValue = clampedValue.toFloat()
            if (sliderValue.floatValue != targetValue) {
                sliderValue.floatValue = targetValue
            }
        }
    }

    LaunchedEffect(manualValue) {
        val normalizedText = manualValue.toString()
        if (inputState.text.toString() != normalizedText) {
            inputState.edit {
                replace(0, length, normalizedText)
            }
        }
    }

    LaunchedEffect(inputState, minValue, maxValue, allowOutsideRange) {
        snapshotFlow { inputState.text.toString() }
            .drop(1)
            .collect { nextText ->
                val parsed = nextText.toIntOrNull() ?: return@collect
                val nextValue = if (allowOutsideRange) parsed else parsed.coerceIn(minValue, maxValue)
                if (nextValue != currentValue.value) {
                    onValueChangeState.value(nextValue)
                }
            }
    }

    val sliderContent = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Slider(
                value = sliderValue.floatValue,
                onValueChange = { next ->
                    isSliderDragging = true
                    sliderValue.floatValue = next.coerceIn(minValue.toFloat(), maxValue.toFloat())

                    val safeSliderValue = sliderValue.floatValue
                    val roundedValue =
                        when {
                            !safeSliderValue.isFinite() -> minValue
                            safeSliderValue >= Int.MAX_VALUE.toFloat() -> Int.MAX_VALUE
                            safeSliderValue <= Int.MIN_VALUE.toFloat() -> Int.MIN_VALUE
                            safeSliderValue >= 0f -> (safeSliderValue + 0.5f).toInt()
                            else -> (safeSliderValue - 0.5f).toInt()
                        }
                    val nextValue =
                        when {
                            roundedValue < minValue -> minValue
                            roundedValue > maxValue -> maxValue
                            else -> roundedValue
                        }
                    if (nextValue != currentValue.value) {
                        onValueChangeState.value(nextValue)
                    }
                },
                onValueChangeFinished = {
                    isSliderDragging = false
                    sliderValue.floatValue = clampedValue.toFloat()
                },
                valueRange = minValue.toFloat()..maxValue.toFloat(),
                steps = steps,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )

            if (showManualInput) {
                TextField(
                    state = inputState,
                    modifier = Modifier.widthIn(min = 56.dp, max = 96.dp),
                    outline = if (error) Outline.Error else Outline.None,
                    readOnly = !enabled,
                    enabled = enabled,
                )
            }
        }
    }

    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    sliderContent()
                }
            }

            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    sliderContent()
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormOptionalSliderField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    defaultValue: Int,
    label: String,
    valueRange: IntRange,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
    showManualInput: Boolean = false,
    allowOutsideRange: Boolean = false,
    steps: Int = 0,
) {
    require(!valueRange.isEmpty()) { "valueRange must not be empty" }

    val minValue = valueRange.first
    val maxValue = valueRange.last
    val normalizedDefaultValue = if (allowOutsideRange) defaultValue else defaultValue.coerceIn(minValue, maxValue)
    val isChecked = value != null
    val effectiveValue = value ?: normalizedDefaultValue

    val fullContent = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { checked ->
                    if (checked) {
                        onValueChange(value ?: normalizedDefaultValue)
                    } else {
                        onValueChange(null)
                    }
                },
                outline = if (error) Outline.Error else Outline.None,
                enabled = enabled,
            )

            FormSliderField(
                value = effectiveValue,
                onValueChange = { nextValue -> onValueChange(nextValue) },
                label = label,
                valueRange = valueRange,
                modifier = Modifier.weight(1f),
                hint = null,
                error = error,
                enabled = enabled && isChecked,
                labelPosition = labelPosition,
                showManualInput = showManualInput,
                allowOutsideRange = allowOutsideRange,
                steps = steps,
            )
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormFloatSliderField(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Inline,
    showManualInput: Boolean = false,
    allowOutsideRange: Boolean = false,
    steps: Int = 0,
) {
    require(!valueRange.isEmpty()) { "valueRange must not be empty" }

    val minValue = valueRange.start
    val maxValue = valueRange.endInclusive
    val clampedValue = value.coerceIn(minValue, maxValue)
    val manualValue = if (allowOutsideRange) value else clampedValue
    var isSliderDragging by remember { mutableStateOf(false) }
    val sliderValue = remember { mutableFloatStateOf(clampedValue) }

    val inputState = remember { TextFieldState(manualValue.toString()) }
    val currentValue = rememberUpdatedState(manualValue)
    val onValueChangeState = rememberUpdatedState(onValueChange)

    LaunchedEffect(clampedValue, isSliderDragging) {
        if (!isSliderDragging) {
            if (sliderValue.floatValue != clampedValue) {
                sliderValue.floatValue = clampedValue
            }
        }
    }

    LaunchedEffect(manualValue) {
        val normalizedText = manualValue.toString()
        if (inputState.text.toString() != normalizedText) {
            inputState.edit {
                replace(0, length, normalizedText)
            }
        }
    }

    LaunchedEffect(inputState, minValue, maxValue, allowOutsideRange) {
        snapshotFlow { inputState.text.toString() }
            .drop(1)
            .collect { nextText ->
                val parsed = nextText.toFloatOrNull() ?: return@collect
                val nextValue = if (allowOutsideRange) parsed else parsed.coerceIn(minValue, maxValue)
                if (nextValue != currentValue.value) {
                    onValueChangeState.value(nextValue)
                }
            }
    }

    val sliderContent = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Slider(
                value = sliderValue.floatValue,
                onValueChange = { next ->
                    isSliderDragging = true
                    sliderValue.floatValue = next.coerceIn(minValue, maxValue)

                    val nextValue = sliderValue.floatValue
                    if (nextValue != currentValue.value) {
                        onValueChangeState.value(nextValue)
                    }
                },
                onValueChangeFinished = {
                    isSliderDragging = false
                    sliderValue.floatValue = clampedValue
                },
                valueRange = minValue..maxValue,
                steps = steps,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )

            if (showManualInput) {
                TextField(
                    state = inputState,
                    modifier = Modifier.widthIn(min = 56.dp, max = 96.dp),
                    outline = if (error) Outline.Error else Outline.None,
                    readOnly = !enabled,
                    enabled = enabled,
                )
            }
        }
    }

    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    sliderContent()
                }
            }

            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    sliderContent()
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = JewelTheme.defaultTextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    placeholder: @Composable (() -> Unit)? = null,
    style: TextAreaStyle = JewelTheme.textAreaStyle,
) {
    val state = remember { TextFieldState(value) }
    val currentValue = rememberUpdatedState(value)

    LaunchedEffect(value) {
        if (state.text.toString() != value) {
            state.edit {
                replace(0, length, value)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .drop(1)
            .collect { nextValue ->
                if (nextValue != currentValue.value) {
                    onValueChange(nextValue)
                }
            }
    }

    FormTextarea(
        state = state,
        label = label,
        modifier = modifier,
        hint = hint,
        error = error,
        readOnly = readOnly,
        enabled = enabled,
        labelPosition = labelPosition,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        placeholder = placeholder,
        style = style,
    )
}

@Composable
fun FormTextarea(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = JewelTheme.defaultTextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    placeholder: @Composable (() -> Unit)? = null,
    style: TextAreaStyle = JewelTheme.textAreaStyle,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(label, style = JewelTheme.typography.labelTextStyle, modifier = Modifier.widthIn(min = 80.dp))
                    TextArea(
                        state,
                        modifier = Modifier
                            .fillMaxSize(),
                        enabled = enabled,
                        readOnly = readOnly,
                        inputTransformation = inputTransformation,
                        textStyle = textStyle,
                        keyboardOptions = keyboardOptions,
                        onKeyboardAction = onKeyboardAction,
                        interactionSource = interactionSource,
                        style = style,
                        outline = if (error) Outline.Error else Outline.None,
                        placeholder = placeholder,
                    )
                }
            }

            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    TextArea(
                        state,
                        Modifier.fillMaxSize(),
                        enabled = enabled,
                        readOnly = readOnly,
                        inputTransformation = inputTransformation,
                        textStyle = textStyle,
                        keyboardOptions = keyboardOptions,
                        onKeyboardAction = onKeyboardAction,
                        interactionSource = interactionSource,
                        style = style,
                        outline = if (error) Outline.Error else Outline.None,
                        placeholder = placeholder,
                    )
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            })
            {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun FormPathField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
) {
    val selectDirectoryTitle = stringResource(Res.string.form_path_select_directory_title)
    val interactionSource = remember { MutableInteractionSource() }
    val state = remember { TextFieldState(value) }
    val currentValue = rememberUpdatedState(value)
    val fileLauncher = rememberDirectoryPickerLauncher(
        dialogSettings = FileKitDialogSettings(
            title = selectDirectoryTitle,
            parentWindow = LocalAppWindow.current
        )
    ) { directory ->
        val path = directory?.absolutePath()
        if (path != null) {
            onValueChange(path)
        }
    }

    LaunchedEffect(value) {
        if (state.text.toString() != value) {
            state.edit {
                replace(0, length, value)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .drop(1)
            .collect { nextValue ->
                if (nextValue != currentValue.value) {
                    onValueChange(nextValue)
                }
            }
    }

    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        label,
                        style = JewelTheme.typography.labelTextStyle,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = fileLauncher::launch,
                            enabled = enabled,
                        ),
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            state,
                            Modifier.fillMaxWidth(),
                            outline = if (error) Outline.Error else Outline.None,
                            readOnly = true,
                            interactionSource = interactionSource,
                            enabled = enabled,
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = fileLauncher::launch,
                                    enabled = enabled,
                                )
                        )
                    }
                }
            }

            LabelPosition.Above -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        label,
                        style = JewelTheme.typography.labelTextStyle,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = fileLauncher::launch,
                            enabled = enabled,
                        ),
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            state,
                            Modifier.fillMaxWidth(),
                            outline = if (error) Outline.Error else Outline.None,
                            interactionSource = interactionSource,
                            readOnly = true,
                            enabled = enabled
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = fileLauncher::launch,
                                    enabled = enabled,
                                )
                        )
                    }
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            })
            {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

@Composable
fun <T : Any> FormSelectField(
    value: T?,
    onValueChange: (T?) -> Unit,
    options: List<T>,
    optionLabel: (T?) -> String,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    enabled: Boolean = true,
    labelPosition: LabelPosition = LabelPosition.Above,
    allowNone: Boolean = true,
    compareWith: (current: T?, to: T?) -> Boolean = { current, to -> current == to}
) {
    val displayOptions = if (allowNone) listOf<T?>(null) + options else options
    val displayLabels = displayOptions.map { optionLabel(it) }
    val selectedIndex = displayOptions.indexOfFirst{ compareWith(value, it) }.takeIf { it >= 0 } ?: 0

    val fullContent = @Composable {
        when (labelPosition) {
            LabelPosition.Inline -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    ListComboBox(
                        items = displayLabels,
                        selectedIndex = selectedIndex,
                        onSelectedItemChange = { index ->
                            if (index >= 0 && index < displayOptions.size) {
                                onValueChange(displayOptions[index])
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        itemKeys = { _, item -> item }
                    )
                }
            }
            LabelPosition.Above -> {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = JewelTheme.typography.labelTextStyle)
                    ListComboBox(
                        items = displayLabels,
                        selectedIndex = selectedIndex,
                        onSelectedItemChange = { index ->
                            if (index >= 0 && index < displayOptions.size) {
                                onValueChange(displayOptions[index])
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        itemKeys = { _, item -> item }
                    )
                }
            }
        }
    }

    Row(modifier) {
        if (hint != null) {
            Tooltip(tooltip = {
                Text(hint, style = JewelTheme.typography.labelTextStyle)
            }) {
                fullContent()
            }
        } else {
            fullContent()
        }
    }
}

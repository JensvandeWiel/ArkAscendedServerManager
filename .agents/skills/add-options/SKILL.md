---
name: add-option
description: >
  Guide to add options to the application,. these options are used for ark server configuration
---

# Step by step guide
## 1. Dissect user request
The user will give you, or ini settings like `AllowHitMarkers` check if it exists in `ArkServerOptions.csv` and check if compatible with ASA by checking the `inASA` column, if `No`, tell user its not compatible, if `Unknown` warn user in chat but continue.
## 2. Find config option location
Check `ArkServerOptions.csv` for the given option, and check the location.
- For GameUserSettings.ini: `core/src/main/kotlin/ini/GameUserSettings.kt`
- For Game.ini: `core/src/main/kotlin/ini/Game.kt`
- For Command line options that are booleans: `core/src/main/kotlin/server/Settings.kt > Options`
- For Command line options that are other types: `core/src/main/kotlin/server/Settings.kt > Administration`
## 3. Add config option
Add the option to the selected file, derive attribute usage for correct setup also get default value from `ArkServerOptions.csv` also add `@Unsure` annotation if in the csv `inASA` is `Unknown`

**Important Data Type and Unit Considerations**:
- If an option in `ArkServerOptions.csv` represents a **time value** (like seconds), and the user requests it to be displayed in **minutes**, ALWAYS store the underlying value in the configuration model (Game.kt/GameUserSettings.kt) as an `Int` to avoid floating point precision issues when calculating and saving. 
- For floating point values (multipliers, generic percentages), use `Float`.
- Perform the conversion (e.g., `value / 60` and `newval * 60`) strictly within the UI layer (`ui/src/main/kotlin/features/server/` files).

## 4. Add validaion
If the info tells about certain limits or its required etc add validation functions, make sure to update the main validation function of the section when adding field validation.
## 5. Select field type
Find the best fitting field in `ui/src/main/kotlin/components/FormField.kt`

**Form Field Index & Selection Guide**:
- `GroupHeader`: Used to create a visual header/title for a section of related settings.
  ```kotlin
  GroupHeader(stringResource(Res.string.server_details_rules_tab))
  ```
- `CheckboxSectionHeader`: Similar to `GroupHeader` but includes a toggleable checkbox to enable/disable the entire section.
  ```kotlin
  CheckboxSectionHeader(
      checked = tributeDownloadsEnabled,
      onCheckedChange = { ... },
      label = stringResource(Res.string.server_details_enable_tribute_downloads_label),
  )
  ```
- `FormCheckboxField`: Use for standard `Boolean` settings.
  ```kotlin
  FormCheckboxField(
      gameUserSettings.serverSettings.serverHardcore,
      onCheckedChange = { newval -> ... },
      label = stringResource(Res.string.server_details_rules_enable_hardcore_mode_label),
      hint = stringResource(Res.string.server_details_rules_enable_hardcore_mode_hint)
  )
  ```
- `FormSliderField`: For `Int` values. Provides a slider with an optional manual text input.
  ```kotlin
  FormSliderField(
      value = game.shooterGameMode.maxNumberOfPlayersInTribe,
      onValueChange = { newval -> ... },
      label = stringResource(Res.string.server_details_rules_max_players_in_tribe_label),
      valueRange = 0..100,
      showManualInput = true,
      allowOutsideRange = true,
      labelPosition = LabelPosition.Above
  )
  ```
- `FormFloatSliderField`: For `Float` values (multipliers, percentages).
  ```kotlin
  FormFloatSliderField(
      value = game.shooterGameMode.customRecipeEffectivenessMultiplier,
      onValueChange = { newval -> ... },
      label = stringResource(Res.string.server_details_rules_custom_recipe_effectiveness_multiplier_label),
      valueRange = 0.1f..5.0f,
      showManualInput = true,
      allowOutsideRange = true,
      labelPosition = LabelPosition.Above
  )
  ```
- `FormTextField`: For standard `String` inputs.
  ```kotlin
  FormTextField(
      value = serverName,
      onValueChange = { newval -> ... },
      label = stringResource(Res.string.server_details_server_name_label),
  )
  ```
- `FormNullableTextField`: For `String?` inputs where an empty string is treated as `null`.
- `FormOptionalTextField`: For `String?` inputs that are enabled with a checkbox.
- `FormOptionalSliderField`: For `Int?` inputs enabled with a checkbox.
- `FormToggleableNullableTextField`: For a string field whose enabled state is controlled by a separate boolean.
- `FormTextarea`: For multiline `String` inputs.
  ```kotlin
  FormTextarea(
      value = motd,
      onValueChange = { newval -> ... },
      label = stringResource(Res.string.server_details_motd_message_label),
  )
  ```
- `FormPathField`: For directory or file path selection.
  ```kotlin
  FormPathField(
      value = installationPath,
      onValueChange = { newval -> ... },
      label = stringResource(Res.string.server_details_installation_path_label),
  )
  ```
- `FormSelectField`: For dropdown selection from a list.
  ```kotlin
  FormSelectField(
      value = selectedMap,
      onValueChange = { newval -> ... },
      options = availableMaps,
      optionLabel = { it.displayName },
      label = stringResource(Res.string.server_details_map_label),
  )
  ```

**Layout Positioning**: 
Do not place new full-width horizontal sliders or text fields inside the 3-column grid structure used for checkboxes. Put them underneath or above the columns so they span the entire width.

## 6. Place the field in the designated spot
The user will tell where to place it based on a tab and sometimes a section these files are at the `ui/src/main/kotlin/features/server` dir.

Add the field here, derive implementation details from the tab/section files to know how to implement it.

**Conditional Rendering**:
If an option requires another option to be enabled/disabled, use the `enabled = ...` property on the `FormField` instead of completely hiding the field with an `if` block, unless specified otherwise. Invert labels intuitively if a setting is negative (e.g., `PreventDiseases` -> "Enable Diseases", `enabled = !preventDiseases`).

## 7. Add label and hint
Add the label and hint based of `ArkServerOptions.csv` or user input, localize this using `ui/src/main/composeResources/values` directory xml files.

**Hint Consistency**:
Always modify the hint strings to align with how the manager handles the input. For example, if Ark expects seconds but the manager UI takes minutes, change the hint to say "(minutes)" and update any examples in the hint text to reflect minutes rather than seconds.

## 8. Update CSV Tracker
When an option is fully implemented, you must modify `ArkServerOptions.csv`. Change the "implemented" column (the last column, after `type`) to `Yes`. If the `implemented` column does not exist yet at the end of the file, you must add it. If the option was completely missing from the CSV, append a new line for it with the appropriate details, ensuring `implemented` is set to `Yes`.

## 9. Summarize
Give all implemented options, labels, hints defaults in a ordered list
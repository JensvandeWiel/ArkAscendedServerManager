---
name: add-server-ini-feature
description: Add a new server INI value to the Ark Ascended Server Manager codebase when given the key, type, placement, and display name.
---

# Add Server INI Feature

Use this skill when you want to add a new server-related INI setting from a short request containing:

- **INI value name** — the exact key or property name
- **Type** — for example `String`, `Int`, `Boolean`, `Float`, or `List<String>`
- **Placement** — where it belongs in the model or INI file structure
- **Display name** — the user-facing label shown in the server feature UI

## What this skill should do

1. Find the right home for the value:
   - `core/src/main/kotlin/server/Settings.kt` for server launch options and startup-script settings
   - `core/src/main/kotlin/ini/GameUserSettings.kt` for `GameUserSettings.ini`
   - a new nested data class if the value does not fit an existing group cleanly
2. Add the property with the correct Kotlin type and a safe default.
3. Annotate it for INI serialization if the value belongs in an INI file.
4. Add validation when the type needs it, such as range checks or non-blank checks.
5. Expose the value in `ui/src/main/kotlin/features/server/ServerScreen.kt` under the correct group.
6. Wire it through `ui/src/main/kotlin/features/server/ServerComponent.kt` if a new updater function is needed.
7. Update startup-script generation in `core/src/main/kotlin/db/models/Server.kt` if the new value must affect the launch command.
8. Add or update tests for validation, serialization, and generated command output.

## Placement rules

- Put **launch flags** in `Settings.Options` or `Settings.Administration`, depending on whether the value changes the startup command.
- Put **INI file values** in `GameUserSettings` and its nested sections.
- Put **display-only UI values** in the server screen only if they are derived from existing state.
- If placement is ambiguous, prefer the module where the value is actually persisted.

## Type mapping

- `String` → `String`
- `Int` → `Int`
- `Boolean` → `Boolean`
- `Float`/`Double` → `Float`/`Double`
- comma-separated values → `List<String>` with parsing and trimming

## Output format

When completing the task, return:

- the new or changed field name
- the file(s) changed
- any validation rules added
- whether startup-script generation changed
- any tests run or recommended next checks

## Example request

> Add `KickIdlePlayersPeriod` as an `Int` in `ServerSettings`, show it in the server feature UI, and keep it under the existing save settings group.

## Example response expectation

- Add the property to the correct nested settings class
- Update the server screen to edit it
- Preserve backwards-compatible defaults
- Verify the build or targeted tests


package ui.server

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.input.TextFieldValue
import server.AdministrationConfig
import ui.ValidationResult

data class GeneralConfigurationModel(
    val serverName: TextFieldValue,
    val serverPasswordEnabled: Boolean,
    val serverPassword: TextFieldState,
    val adminPassword: TextFieldState,
    val serverPort: TextFieldValue,
    val queryPort: TextFieldValue,
    val rconPort: TextFieldValue,
    val rconEnabled: Boolean,
    val rconPassword: TextFieldState,
    val map: TextFieldValue,
    val slots: TextFieldValue,
    val mods: MutableList<Int> = mutableListOf()
) {
    fun toAdministrationConfig(): AdministrationConfig {
        return AdministrationConfig(
            serverName = serverName.text,
            serverPassword = if (serverPasswordEnabled) serverPassword.text.toString() else null,
            adminPassword = adminPassword.text.toString(),
            serverPort = serverPort.text.toIntOrNull() ?: 7777,
            queryPort = queryPort.text.toIntOrNull() ?: 27015,
            rconPort = rconPort.text.toIntOrNull() ?: 27016,
            rconEnabled = rconEnabled,
            rconPassword = rconPassword.text.toString(),
            map = map.text,
            slots = slots.text.toIntOrNull() ?: 70,
            mods = mods.toList()
        )
    }

    companion object {
        fun fromAdministrationConfig(administrationConfig: AdministrationConfig): GeneralConfigurationModel {
            return GeneralConfigurationModel(
                serverName = TextFieldValue(administrationConfig.serverName),
                serverPasswordEnabled = administrationConfig.serverPassword != null,
                serverPassword = TextFieldState(administrationConfig.serverPassword ?: ""),
                adminPassword = TextFieldState(administrationConfig.adminPassword),
                serverPort = TextFieldValue(administrationConfig.serverPort.toString()),
                queryPort = TextFieldValue(administrationConfig.queryPort.toString()),
                rconPort = TextFieldValue(administrationConfig.rconPort.toString()),
                rconEnabled = administrationConfig.rconEnabled,
                rconPassword = TextFieldState(
                    administrationConfig.rconPassword ?: ""
                ),
                map = TextFieldValue(administrationConfig.map),
                slots = TextFieldValue(administrationConfig.slots.toString()),
                mods = administrationConfig.mods.toMutableList()
            )
        }
    }


    fun validate(): ValidationResult<GeneralConfigurationModel> {
        if (serverName.text.isBlank()) {
            return ValidationResult.invalid("Server name cannot be empty")
        }

        if (serverName.text.length > 63) {
            return ValidationResult.invalid("Server name cannot exceed 63 characters")
        }

        if (serverPasswordEnabled) {
            if (serverPassword.text.isBlank()) {
                return ValidationResult.invalid("Server password cannot be empty")
            }
        }

        if (adminPassword.text.isBlank()) {
            return ValidationResult.invalid("Admin password cannot be empty")
        }

        if (adminPassword.text.length < 8) {
            return ValidationResult.invalid("Admin password must be at least 8 characters long")
        }

        if (serverPort.text.isBlank() || !serverPort.text.matches(Regex("\\d+"))) {
            return ValidationResult.invalid("Server port must be a valid number")
        }

        val port = serverPort.text.toIntOrNull()
        if (port == null || port < 1 || port > 65535) {
            return ValidationResult.invalid("Server port must be between 1 and 65535")
        }

        if (queryPort.text.isBlank() || !queryPort.text.matches(Regex("\\d+"))) {
            return ValidationResult.invalid("Query port must be a valid number")
        }

        val queryPortValue = queryPort.text.toIntOrNull()
        if (queryPortValue == null || queryPortValue < 1 || queryPortValue > 65535) {
            return ValidationResult.invalid("Query port must be between 1 and 65535")
        }

        if (serverPort.text == queryPort.text) {
            return ValidationResult.invalid("Server port and query port cannot be the same")
        }

        if (rconPort.text.isBlank() || !rconPort.text.matches(Regex("\\d+"))) {
            return ValidationResult.invalid("RCON port must be a valid number")
        }

        val rconPortValue = rconPort.text.toIntOrNull()
        if (rconPortValue == null || rconPortValue < 1 || rconPortValue > 65535) {
            return ValidationResult.invalid("RCON port must be between 1 and 65535")
        }

        if (rconEnabled && rconPassword.text.isBlank()) {
            return ValidationResult.invalid("RCON password cannot be empty when RCON is enabled")
        }

        if (rconEnabled && rconPassword.text.length < 8) {
            return ValidationResult.invalid("RCON password must be at least 8 characters long when RCON is enabled")
        }

        if (rconEnabled && rconPort.text == serverPort.text) {
            return ValidationResult.invalid("RCON port and server port cannot be the same")
        }

        if (rconEnabled && rconPort.text == queryPort.text) {
            return ValidationResult.invalid("RCON port and query port cannot be the same")
        }

        if (map.text.isBlank()) {
            return ValidationResult.invalid("Map cannot be empty")
        }

        if (slots.text.isBlank() || !slots.text.matches(Regex("\\d+"))) {
            return ValidationResult.invalid("Slots must be a valid number")
        }

        val slotsValue = slots.text.toIntOrNull()
        if (slotsValue == null || slotsValue < 1) {
            return ValidationResult.invalid("Slots must be at least 1")
        }

        return ValidationResult.valid()
    }
}

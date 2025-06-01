package ui.server

import androidx.compose.ui.text.input.TextFieldValue
import server.ServerProfile
import ui.ValidationResult

data class ProfileConfigurationModel(val profileName: TextFieldValue,
                                     val installationLocation: TextFieldValue) {
    companion object {
        fun fromServerProfile(serverProfile: ServerProfile): ProfileConfigurationModel {
            return ProfileConfigurationModel(
                profileName = TextFieldValue(serverProfile.profileName),
                installationLocation = TextFieldValue(serverProfile.installationLocation)
            )
        }
    }

    fun validate(): ValidationResult<ProfileConfigurationModel> {
        if (profileName.text.isBlank()) {
            return ValidationResult.invalid("Profile name cannot be empty")
        }

        if (profileName.text.length > 50) {
            return ValidationResult.invalid("Profile name cannot exceed 50 characters")
        }

        if (installationLocation.text.isBlank()) {
            return ValidationResult.invalid("Installation location cannot be empty")
        }

        if (installationLocation.text.length > 255) {
            return ValidationResult.invalid("Installation location cannot exceed 255 characters")
        }

        // Check if the installation location is a valid windows path
        if (!installationLocation.text.matches(Regex("^[a-zA-Z]:\\\\(?:[^\\\\/:*?\"<>|]+\\\\)*[^\\\\/:*?\"<>|]*\$"))) {
            return ValidationResult.invalid("Installation location must be a valid Windows path")
        }

        return ValidationResult.valid()
    }
}
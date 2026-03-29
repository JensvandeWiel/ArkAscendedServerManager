package eu.wynq.arkascendedservermanager.ui.helpers

import java.nio.file.InvalidPathException
import java.nio.file.Paths

data class ValidationResult(
    val isValid: Boolean,
    val reason: String? = null,
)

private const val MAX_DEFAULT_PATH_LENGTH = 255

fun validatePath(path: String, maxLength: Int = MAX_DEFAULT_PATH_LENGTH): ValidationResult {
    if (path.isBlank()) {
        return ValidationResult(isValid = false, reason = "Data path cannot be blank")
    }

    if (path.length > maxLength) {
        return ValidationResult(
            isValid = false,
            reason = "Data path cannot exceed $maxLength characters",
        )
    }

    val parsedPath = try {
        Paths.get(path)
    } catch (_: InvalidPathException) {
        return ValidationResult(isValid = false, reason = "Data path is not a valid file system path")
    }

    if (!parsedPath.isAbsolute) {
        return ValidationResult(isValid = false, reason = "Data path must be absolute")
    }

    return ValidationResult(isValid = true)
}

fun isValidPath(path: String, maxLength: Int = MAX_DEFAULT_PATH_LENGTH): Boolean = validatePath(path, maxLength).isValid


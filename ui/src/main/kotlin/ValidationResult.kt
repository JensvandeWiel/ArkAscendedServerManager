package ui

data class ValidationResult<T>(val error: String? = null) {
    val isValid: Boolean
        get() = error == null

    companion object {
        fun <T> valid(): ValidationResult<T> = ValidationResult()

        fun <T> invalid(error: String): ValidationResult<T> = ValidationResult(error)
    }
}

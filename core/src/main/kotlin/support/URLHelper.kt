package eu.wynq.arkascendedservermanager.core.support

import io.ktor.http.URLParserException
import io.ktor.http.Url

fun String.isValidUrl(): Boolean {
    val trimmed = trim()
    if (!trimmed.startsWith("http://", ignoreCase = true) &&
        !trimmed.startsWith("https://", ignoreCase = true)
    ) {
        return false
    }
    if (trimmed.substringAfter("://", "").isBlank()) {
        return false
    }
    return try {
        Url(trimmed).host.isNotBlank()
    } catch (e: URLParserException) {
        false
    }
}
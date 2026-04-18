package eu.wynq.arkascendedservermanager.core.support

import io.ktor.http.URLParserException
import io.ktor.http.Url

fun String.isValidUrl(): Boolean {
    return try {
        Url(this)
        true
    } catch (e: URLParserException) {
        false
    }
}
package eu.wynq.arkascendedservermanager.ui.helpers

object AppChangelog {
    private const val CHANGELOG_RESOURCE = "app-changelog.md"

    private val markdown: String by lazy {
        AppChangelog::class.java.classLoader
            .getResourceAsStream(CHANGELOG_RESOURCE)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?.takeIf { it.isNotBlank() }
            ?: "## Changelog unavailable\n\nNo embedded changelog found in application resources."
    }

    fun forCurrentVersion(version: String): String {
        val normalizedVersion = version.trim().removePrefix("v")
        if (normalizedVersion.isBlank()) {
            return "No changelog entries found for this version."
        }

        val headingCandidates = listOf("## v$normalizedVersion", "## $normalizedVersion")
        val lines = markdown.lines()
        val startIndex = lines.indexOfFirst { line -> headingCandidates.any { it == line.trim() } }
        if (startIndex == -1) {
            return "No changelog entries found for v$normalizedVersion."
        }

        val endIndex = lines.subList(startIndex + 1, lines.size)
            .indexOfFirst { it.trim().startsWith("## ") }
            .let { if (it == -1) lines.size else startIndex + 1 + it }

        return lines
            .subList(startIndex + 1, endIndex)
            .dropWhile { it.isBlank() }
            .joinToString("\n")
            .ifBlank { "No changelog entries found for v$normalizedVersion." }
            .trim()
    }
}



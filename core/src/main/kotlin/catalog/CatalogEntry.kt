package eu.wynq.arkascendedservermanager.core.catalog

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * One searchable class name from the shipped ASA catalog.
 *
 * [asa] is `yes` for entries found on an ASA-specific wiki page, `unknown` for
 * entries that only exist in the legacy ASE game data (they may still work,
 * but the UI should not present them as verified).
 */
@Serializable
data class CatalogEntry(
    val name: String,
    val className: String,
    val category: String? = null,
    val nameTag: String? = null,
    val mod: String? = null,
    val asa: String = "unknown",
    val aliases: List<String> = emptyList(),
) {
    val isAsaVerified: Boolean get() = asa == "yes"

    fun matches(query: String): Boolean {
        val tokens = query.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.isEmpty()) return true
        return tokens.all { token ->
            name.contains(token, ignoreCase = true) ||
                    className.contains(token, ignoreCase = true) ||
                    category?.contains(token, ignoreCase = true) == true ||
                    nameTag?.contains(token, ignoreCase = true) == true ||
                    aliases.any { it.contains(token, ignoreCase = true) }
        }
    }
}

@Serializable
data class Catalog(
    val source: String = "",
    val generatedAt: String = "",
    val count: Int = 0,
    val entries: List<CatalogEntry> = emptyList(),
)

object ArkCatalogs {
    private val json = Json { ignoreUnknownKeys = true }
    private val catalogs = mutableMapOf<String, Catalog>()

    val creatures: List<CatalogEntry> by lazy { catalog("creatures").entries }
    val engrams: List<CatalogEntry> by lazy { catalog("engrams").entries }
    val items: List<CatalogEntry> by lazy { catalog("items").entries }
    val resources: List<CatalogEntry> by lazy { catalog("resources").entries }

    /** Generation date of the shipped catalogs, for display in the table editors. */
    val generatedAt: String by lazy { catalog("creatures").generatedAt }

    @Synchronized
    private fun catalog(kind: String): Catalog {
        return catalogs.getOrPut(kind) {
            val stream = ArkCatalogs::class.java.getResourceAsStream("/catalogs/asa/$kind.json")
                ?: return@getOrPut Catalog()
            stream.use { input ->
                json.decodeFromString<Catalog>(input.reader(Charsets.UTF_8).readText())
            }
        }
    }

    fun load(kind: String): List<CatalogEntry> = catalog(kind).entries

    fun search(entries: List<CatalogEntry>, query: String, limit: Int = 100): List<CatalogEntry> {
        val trimmed = query.trim()
        val filtered = if (trimmed.isEmpty()) entries else entries.filter { it.matches(trimmed) }
        return filtered.take(limit)
    }
}

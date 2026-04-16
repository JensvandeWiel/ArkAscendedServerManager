package eu.wynq.arkascendedservermanager.core.server

import eu.wynq.arkascendedservermanager.core.server.ArgOption
import kotlinx.serialization.Serializable
import java.lang.reflect.Field
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.random.Random

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArgOption(
    val name: String,
    val kind: ArgKind = ArgKind.QUERY,
    val order: Int = Int.MAX_VALUE,
    val force: Boolean = false,
)

enum class ArgKind {
    RAW,
    QUERY,
    BOOLEAN_QUERY,
    FLAG_VALUE,
    FLAG,
    LIST,
}

@Serializable
data class Settings(
    val administration: Administration = Administration(),
    val options: Options = Options()
) {
    fun validate() = administration.validate() && options.validate()

    fun toStartupScriptArguments(): List<String> = administration.toStartupScriptArguments() + options.getEnabledOptions()

    companion object {
        fun createForNewServer(): Settings {
            return Settings(
                Administration(),
                Options()
            )
        }

        fun fromStartupScriptString(startupScript: String): Settings {
            val parsed = parseStartupScript(startupScript)
            return Settings(
                administration = Administration.fromParsedStartupScript(parsed),
                options = Options.fromParsedStartupScript(parsed)
            )
        }
    }
}

@Serializable
data class Administration(
    @field:ArgOption(name = "Map", kind = ArgKind.RAW, order = 0)
    val map: String = "TheIsland_WP",

    @field:ArgOption(name = "Port", kind = ArgKind.QUERY, order = 1)
    val serverPort: Int = 7777,


    @field:ArgOption(name = "QueryPort", kind = ArgKind.QUERY, order = 2)
    val queryPort: Int = 27015,

    @field:ArgOption(name = "RCONEnabled", kind = ArgKind.BOOLEAN_QUERY, order = 3)
    val rconEnabled: Boolean = true,

    @field:ArgOption(name = "RCONPort", kind = ArgKind.QUERY, order = 4)
    val rconPort: Int = 27016,

    @field:ArgOption(name = "ServerPassword", kind = ArgKind.QUERY, order = 5)
    val serverPassword: String? = null,

    @field:ArgOption(name = "ServerAdminPassword", kind = ArgKind.QUERY, order = 6)
    val adminPassword: String = getRandomString(),

    @field:ArgOption(name = "WinLiveMaxPlayers", kind = ArgKind.FLAG_VALUE, order = 7)
    val slots: Int = 70,

    @field:ArgOption(name = "mods", kind = ArgKind.LIST, order = 8)
    val mods: List<String> = emptyList(),

    @field:ArgOption(name = "clusterid", kind = ArgKind.FLAG_VALUE, order = 9)
    val clusterId: String? = null,

    @field:ArgOption(name = "ClusterDirOverride", kind = ArgKind.FLAG_VALUE, order = 10)
    val clusterDirOverride: String? = null,

) {
    fun validate() = validateServerPassword()
            && validateAdminPassword()
            && validateSlots()
            && validateMods()
            && validateMap()
            && validateQueryPort()
            && validatePeerPort()
            && validateRconPort()
            && validateServerPort()

    fun validateServerPassword() = serverPassword == null || serverPassword.isNotBlank()
    fun validateAdminPassword() = adminPassword.isNotBlank()
    fun validateSlots() = slots > 0
    fun validateMods() = mods.all { mod ->
        mod.isNotBlank() && mod.isNumeric()
    }

    fun validateMap() = map.isNotBlank()
    fun validateQueryPort() = queryPort in 1..65535
    fun validatePeerPort() = peerPort in 1..65535 && peerPort == serverPort + 1
    fun validateRconPort() = rconPort in 1..65535
    fun validateServerPort() = serverPort in 1..65535

    val peerPort: Int
        get() = serverPort + 1

    fun toStartupScriptArguments(): List<String> {
        val head = StringBuilder()
        val trailingArguments = mutableListOf<String>()

        startupArgFields(this).forEach { field ->
            val argOption = field.argOptionOrNull() ?: return@forEach
            val value = runCatching {
                field.isAccessible = true
                field.get(this)
            }.getOrNull() ?: return@forEach

            when (argOption.kind) {
                ArgKind.RAW -> (value as? String)?.takeIf { it.isNotBlank() }?.let(head::append)
                ArgKind.QUERY -> {
                    when (value) {
                        is String -> value.takeIf { it.isNotBlank() }?.let { head.append("?${argOption.name}=$it") }
                        else -> head.append("?${argOption.name}=$value")
                    }
                }
                ArgKind.BOOLEAN_QUERY -> {
                    val boolValue = value as? Boolean ?: return@forEach
                    head.append("?${argOption.name}=${if (boolValue) "True" else "False"}")
                }
                ArgKind.FLAG_VALUE -> {
                    when (value) {
                        is String -> value.takeIf { it.isNotBlank() }?.let { trailingArguments.add("-${argOption.name}=$it") }
                        else -> value?.let { trailingArguments.add("-${argOption.name}=$it") }
                    }
                }
                ArgKind.FLAG -> if (argOption.force || value == true) trailingArguments.add("-${argOption.name}")
                ArgKind.LIST -> {
                    val list = value as? List<*> ?: emptyList<Any?>()
                    val sanitized = list.filterIsInstance<String>()
                        .mapNotNull { item -> item.trim().takeIf { it.isNotEmpty() && it.isNumeric() } }
                    if (sanitized.isNotEmpty()) {
                        trailingArguments.add("-${argOption.name}=${sanitized.joinToString(",")}")
                    }
                }
            }
        }

        return buildList {
            if (head.isNotEmpty()) {
                add(head.toString())
            }
            addAll(trailingArguments)
        }
    }

    companion object {
        internal fun fromParsedStartupScript(parsed: ParsedStartupScript): Administration {
            return parseByArgOptions(parsed, customValues = mapOf(
                "rconEnabled" to {
                    parsed.queryValues["RCONEnabled"]?.toBooleanOrNull()
                        ?: if (parsed.queryValues["RCONPort"]?.toIntOrNull() != null) true else Administration().rconEnabled
                }
            ))
        }
    }
}

private fun getRandomString(): String {
    val bytes = Random.nextBytes(8)
    return buildString(bytes.size * 2) {
        bytes.forEach { byte ->
            val value = byte.toInt() and 0xff
            append("0123456789abcdef"[value and 0x0f])
            append("0123456789abcdef"[(value shr 4) and 0x0f])
        }
    }
}

private fun String.isNumeric() = all { it.isDigit() }

private fun String.toBooleanOrNull(): Boolean? = when (trim().lowercase()) {
    "true", "1", "yes" -> true
    "false", "0", "no" -> false
    else -> null
}

internal data class ParsedStartupScript(
    val map: String?,
    val queryValues: Map<String, String>,
    val dashValues: Map<String, String>,
    val flags: Set<String>,
)

private fun parseStartupScript(startupScript: String): ParsedStartupScript {
    val tokens = startupScript.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    val argumentTokens = buildList {
        if (tokens.firstOrNull() == "start") {
            for (index in 2 until tokens.size) {
                add(tokens[index])
            }
        } else {
            addAll(tokens)
        }
    }

    var parsedMap: String? = null
    val queryValues = linkedMapOf<String, String>()
    val dashValues = linkedMapOf<String, String>()
    val flags = linkedSetOf<String>()

    argumentTokens.forEachIndexed { index, token ->
        val segments = token.split('?').filter { it.isNotBlank() }
        if (segments.isEmpty()) {
            return@forEachIndexed
        }

        if (index == 0 && parsedMap == null) {
            val firstSegment = segments.firstOrNull()
            parsedMap = firstSegment?.takeIf { !it.contains('=') && !it.startsWith('-') }
            for (segmentIndex in 1 until segments.size) {
                parseSegment(segments[segmentIndex], queryValues, dashValues, flags)
            }
        } else {
            segments.forEach { segment ->
                parseSegment(segment, queryValues, dashValues, flags)
            }
        }
    }

    return ParsedStartupScript(
        map = parsedMap,
        queryValues = queryValues,
        dashValues = dashValues,
        flags = flags,
    )
}

private fun parseSegment(
    segment: String,
    queryValues: MutableMap<String, String>,
    dashValues: MutableMap<String, String>,
    flags: MutableSet<String>,
) {
    val trimmed = segment.trim()
    if (trimmed.isEmpty()) return

    when {
        trimmed.startsWith("-") -> {
            val value = trimmed.removePrefix("-")
            val pieces = value.split('=', limit = 2)
            if (pieces.size == 2) {
                dashValues[pieces[0]] = pieces[1]
            } else {
                flags += value
            }
        }
        else -> {
            val pieces = trimmed.split('=', limit = 2)
            if (pieces.size == 2) {
                queryValues[pieces[0]] = pieces[1]
            }
        }
    }
}

private fun startupArgFields(instance: Any): List<Field> {
    return instance.javaClass.declaredFields
        .asSequence()
        .filter { field -> field.argOptionOrNull() != null }
        .sortedBy { field -> field.argOptionOrNull()?.order ?: Int.MAX_VALUE }
        .toList()
}

private fun Field.argOptionOrNull(): ArgOption? = annotations.filterIsInstance<ArgOption>().firstOrNull()

private object MissingParsedValue

private inline fun <reified T : Any> parseByArgOptions(
    parsed: ParsedStartupScript,
    forcedValues: Map<String, Any?> = emptyMap(),
    customValues: Map<String, () -> Any?> = emptyMap(),
): T {
    val constructor = T::class.primaryConstructor ?: error("Missing primary constructor for ${T::class.simpleName}")
    val fieldByName = T::class.java.declaredFields.associateBy { it.name }
    val values = mutableMapOf<KParameter, Any?>()

    constructor.parameters.forEach { parameter ->
        val parameterName = parameter.name ?: return@forEach

        if (forcedValues.containsKey(parameterName)) {
            values[parameter] = forcedValues[parameterName]
            return@forEach
        }

        val customValueProvider = customValues[parameterName]
        if (customValueProvider != null) {
            values[parameter] = customValueProvider()
            return@forEach
        }

        val field = fieldByName[parameterName] ?: return@forEach
        val argOption = field.argOptionOrNull() ?: return@forEach
        val parsedValue = parseArgOptionValue(parsed, argOption, parameter)
        if (parsedValue !== MissingParsedValue) {
            values[parameter] = parsedValue
        }
    }

    return constructor.callBy(values)
}

private fun parseArgOptionValue(
    parsed: ParsedStartupScript,
    argOption: ArgOption,
    parameter: KParameter,
): Any? {
    return when (argOption.kind) {
        ArgKind.RAW -> parsed.map?.takeIf { it.isNotBlank() } ?: MissingParsedValue
        ArgKind.QUERY -> {
            val raw = parsed.queryValues[argOption.name] ?: return MissingParsedValue
            parseScalarValue(raw, parameter)
        }
        ArgKind.BOOLEAN_QUERY -> {
            val raw = parsed.queryValues[argOption.name] ?: return MissingParsedValue
            raw.toBooleanOrNull() ?: MissingParsedValue
        }
        ArgKind.FLAG_VALUE -> {
            val raw = parsed.dashValues[argOption.name] ?: return MissingParsedValue
            parseScalarValue(raw, parameter)
        }
        ArgKind.FLAG -> argOption.force || parsed.flags.contains(argOption.name)
        ArgKind.LIST -> {
            val raw = parsed.dashValues[argOption.name] ?: return MissingParsedValue
            raw.split(',').mapNotNull { value -> value.trim().takeIf { it.isNotEmpty() && it.isNumeric() } }
        }
    }
}

private fun parseScalarValue(raw: String, parameter: KParameter): Any? {
    val classifier = parameter.type.classifier
    return when (classifier) {
        Int::class -> raw.toIntOrNull() ?: MissingParsedValue
        String::class -> if (parameter.type.isMarkedNullable) raw.takeIf { it.isNotBlank() } else raw
        Boolean::class -> raw.toBooleanOrNull() ?: MissingParsedValue
        else -> raw
    }
}

@Serializable
data class Options(
    @field:ArgOption(name = "EnableIdlePlayerKick", kind = ArgKind.FLAG, order = 0)
    val enableIdlePlayerKick: Boolean = false,
    @field:ArgOption(name = "OldConsole", kind = ArgKind.FLAG, order = 1, force = true)
    val oldConsole: Boolean = true,
    @field:ArgOption(name = "NoGameAnalytics", kind = ArgKind.FLAG, order = 2, force = true)
    val noGameAnalytics: Boolean = true,
) {
   fun validate() = true

    fun getEnabledOptions(): List<String> {
        return startupArgFields(this)
            .mapNotNull { field ->
                val argOption = field.argOptionOrNull() ?: return@mapNotNull null
                val value = runCatching {
                    field.isAccessible = true
                    field.get(this)
                }.getOrNull()
                if (argOption.kind == ArgKind.FLAG && (argOption.force || value == true)) {
                    "-${argOption.name}"
                } else {
                    null
                }
            }
    }

    companion object {
        internal fun fromParsedStartupScript(parsed: ParsedStartupScript): Options {
            return parseByArgOptions(parsed)
        }
    }
}
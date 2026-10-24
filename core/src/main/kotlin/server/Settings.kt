package eu.wynq.arkascendedservermanager.core.server

import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.core.support.isValidUrl
import kotlinx.serialization.Serializable
import support.Unsure
import java.awt.Stroke
import java.lang.reflect.Field
import kotlin.random.Random
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

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

    fun toStartupScriptArguments(): List<String> =
        administration.toStartupScriptArguments() + options.getEnabledOptions()

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


    @Unsure
    @field:ArgOption(name = "QueryPort", kind = ArgKind.QUERY, order = 2)
    val queryPort: Int = 27015,

    @Unsure
    @field:ArgOption(name = "RCONEnabled", kind = ArgKind.BOOLEAN_QUERY, order = 3)
    val rconEnabled: Boolean = true,

    @field:ArgOption(name = "RCONPort", kind = ArgKind.QUERY, order = 4)
    val rconPort: Int = 27020,

    @field:ArgOption(name = "ServerPassword", kind = ArgKind.QUERY, order = 5)
    val serverPassword: String? = null,

    @field:ArgOption(name = "ServerAdminPassword", kind = ArgKind.QUERY, order = 6)
    val adminPassword: String = getRandomString(),

    @field:ArgOption(name = "WinLiveMaxPlayers", kind = ArgKind.FLAG_VALUE, order = 7)
    val slots: Int = 70,

    // Mods live in GameUserSettings.ini [ServerSettings] ActiveMods.
    // `-mods` is still parsed for legacy imports, but is never emitted unless the
    // user explicitly enables the mirror toggle below.
    val mods: List<String> = emptyList(),

    /**
     * App-only. When true, the launch line also gets `-mods=<ActiveMods>`.
     * ASA documents `-mods=` as the route that downloads/updates CurseForge mods;
     * the wiki also says the engine reads ActiveMods. Keep the lists identical.
     */
    val mirrorModsToLaunchArgs: Boolean = false,

    @field:ArgOption(name = "clusterid", kind = ArgKind.FLAG_VALUE, order = 9)
    val clusterId: String? = null,

    @field:ArgOption(name = "ClusterDirOverride", kind = ArgKind.FLAG_VALUE, order = 10)
    val clusterDirOverride: String? = null,

    @field:ArgOption(name = "culture", kind = ArgKind.FLAG_VALUE, order = 11)
    val culture: String? = null,
    @field:ArgOption(name = "ActiveEvent", kind = ArgKind.FLAG_VALUE, order = 12)
    val activeEvent: String? = null,

    @field:ArgOption(name = "GBUsageToForceRestart", kind = ArgKind.FLAG_VALUE, order = 20)
    val gbUsageToForceRestart: Float? = null,
    @Unsure
    @field:ArgOption(name = "MaxNumOfSaveBackups", kind = ArgKind.FLAG_VALUE, order = 21)
    val maxNumOfSaveBackups: Int? = null,
    @field:ArgOption(name = "AltSaveDirectoryName", kind = ArgKind.QUERY, order = 22)
    val altSaveDirectoryName: String? = null,
    @field:ArgOption(name = "CustomNotificationURL", kind = ArgKind.FLAG_VALUE, order = 23)
    val customNotificationUrl: String? = null,
    @field:ArgOption(name = "ServerIP", kind = ArgKind.FLAG_VALUE, order = 24)
    val serverIp: String? = null,
    @field:ArgOption(name = "ip", kind = ArgKind.FLAG_VALUE, order = 25)
    val ip: String? = null,
    @field:ArgOption(name = "ServerPlatform", kind = ArgKind.FLAG_VALUE, order = 26)
    val serverPlatform: String? = null,

    /** Raw escape hatch; appended to the launch arguments verbatim. */
    val extraArgs: String? = null,

    val restartAfterCrash: Boolean = false,

    val sendServerUpdatesToDiscord: Boolean = false,
    val sendServerUpdatesWebhookUrl: String? = null,
    val sendServerUpdatesEvents: Set<PowerState> = emptySet(),
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
            && validateCulture()
            && validateSendServerUpdatesWebhookUrl()
            && validateActiveEvent()
            && validateGbUsageToForceRestart()
            && validateMaxNumOfSaveBackups()
            && validateCustomNotificationUrl()
            && validateServerIp()
            && validateIp()
            && validateServerPlatform()
            && validateAltSaveDirectoryName()


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
    fun validateCulture() = culture == null || culture.isNotBlank() && locales.contains(culture)
    fun validateActiveEvent() = activeEvent == null || activeEvent.isNotBlank() && activeEvent.split(" ").size == 1
    fun validateGbUsageToForceRestart() = gbUsageToForceRestart == null || gbUsageToForceRestart >= 0
    fun validateMaxNumOfSaveBackups() = maxNumOfSaveBackups == null || maxNumOfSaveBackups >= 0
    fun validateCustomNotificationUrl() = customNotificationUrl == null || (customNotificationUrl.isNotBlank() && customNotificationUrl.isValidUrl())
    fun validateServerIp() = serverIp == null || serverIp.isNotBlank()
    fun validateIp() = ip == null || ip.isNotBlank()
    fun validateServerPlatform() = serverPlatform == null || serverPlatform.isNotBlank()
    fun validateAltSaveDirectoryName() = altSaveDirectoryName == null || altSaveDirectoryName.isNotBlank()
    fun validateSendServerUpdatesWebhookUrl() =
        sendServerUpdatesWebhookUrl == null || sendServerUpdatesWebhookUrl.isNotBlank() && sendServerUpdatesWebhookUrl.isValidUrl()

    val locales = listOf(
        "ca", "cs", "da", "de", "en", "es", "eu", "fi", "fr", "hu",
        "it", "ja", "ka", "ko", "nl", "pl", "pt_BR", "ru", "sv", "th",
        "tr", "zh", "zh-Hans-CN", "zh-TW"
    )

    val peerPort: Int
        get() = serverPort + 1

    fun toStartupScriptArguments(): List<String> =
        buildStartupArguments(this) + listOfNotNull(extraArgs?.takeIf { it.isNotBlank() })

    companion object {
        internal fun fromParsedStartupScript(parsed: ParsedStartupScript): Administration {
            return parseByArgOptions(
                parsed, customValues = mapOf(
                "rconEnabled" to {
                    parsed.queryValues["RCONEnabled"]?.toBooleanOrNull()
                        ?: if (parsed.queryValues["RCONPort"]?.toIntOrNull() != null) true else Administration().rconEnabled
                },
                // Legacy import only: new profiles store mods in ActiveMods.
                "mods" to {
                    val raw: String? = parsed.dashValues["mods"] ?: parsed.queryValues["GameModIds"]
                    raw
                        ?.split(',')
                        ?.mapNotNull { value -> value.trim().takeIf { it.isNotEmpty() && it.isNumeric() } }
                        ?: emptyList()
                }
            ))
        }
    }
}

/**
 * Serializes any class carrying [ArgOption] fields into launch arguments.
 * Shared by [Administration] and [Options], so every [ArgKind] works in both.
 */
private fun buildStartupArguments(instance: Any): List<String> {
    val head = StringBuilder()
    val trailingArguments = mutableListOf<String>()

    startupArgFields(instance).forEach { field ->
        val argOption = field.argOptionOrNull() ?: return@forEach
        val value = runCatching {
            field.isAccessible = true
            field.get(instance)
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
                    is String -> value.takeIf { it.isNotBlank() }
                        ?.let { trailingArguments.add("-${argOption.name}=$it") }

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
    // Tokenize respecting quoted strings
    val tokens = tokenizeStartupString(startupScript)

    val argumentTokens = buildList {
        if (tokens.firstOrNull() == "start") {
            // tokens layout possibilities:
            // old format: [start, path, map?...] -> start args at index 2
            // new format: [start, profileName, path, map?...] -> start args at index 3
            val startIndex = when {
                tokens.size >= 3 && !looksLikePath(tokens[1]) && looksLikePath(tokens[2]) -> 3
                tokens.size >= 2 && looksLikePath(tokens[1]) -> 2
                else -> 2
            }
            for (index in startIndex until tokens.size) add(tokens[index])
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

private fun tokenizeStartupString(input: String): List<String> {
    val result = mutableListOf<String>()
    var i = 0
    val n = input.length
    while (i < n) {
        while (i < n && input[i].isWhitespace()) i++
        if (i >= n) break
        if (input[i] == '"') {
            i++
            val start = i
            while (i < n && input[i] != '"') i++
            val token = input.substring(start, i)
            result.add(token)
            if (i < n && input[i] == '"') i++
        } else {
            val start = i
            while (i < n && !input[i].isWhitespace()) i++
            val token = input.substring(start, i)
            result.add(token)
        }
    }
    return result
}

private fun looksLikePath(token: String): Boolean {
    return token.contains('\\') || token.contains('/') || token.contains(':') || token.endsWith(".exe", ignoreCase = true)
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
    @field:ArgOption(name = "NoTransferFromFiltering", kind = ArgKind.FLAG, order = 11)
    val noTransferFromFiltering: Boolean = true,
    @field:ArgOption(name = "UseDynamicConfig", kind = ArgKind.FLAG, order = 12)
    val useDynamicConfig: Boolean = false,
    @field:ArgOption(name = "NoBattlEye", kind = ArgKind.FLAG, order = 13)
    val noBattlEye: Boolean = false,
    @field:ArgOption(name = "NoDinos", kind = ArgKind.FLAG, order = 14)
    val noDinos: Boolean = false,
    @field:ArgOption(name = "DisableCustomCosmetics", kind = ArgKind.FLAG, order = 15)
    val disableCustomCosmetics: Boolean = false,
    @field:ArgOption(name = "EasterColors", kind = ArgKind.FLAG, order = 16)
    val easterColors: Boolean = false,
    @field:ArgOption(name = "ForceRespawnDinos", kind = ArgKind.FLAG, order = 17)
    val forceRespawnDinos: Boolean = false,
    @field:ArgOption(name = "ServerUseEventColors", kind = ArgKind.FLAG, order = 18)
    val useEventColors: Boolean = false,
    @field:ArgOption(name = "AutoDestroyStructures", kind = ArgKind.FLAG, order = 19)
    val autoDestroyStructures: Boolean = false,
    @Unsure
    @field:ArgOption(name = "UseItemDupeCheck", kind = ArgKind.FLAG, order = 20)
    val useItemDupeCheck: Boolean = false,
    @field:ArgOption(name = "ForceDupeLog", kind = ArgKind.FLAG, order = 21)
    val forceDupeLog: Boolean = false,
    @field:ArgOption(name = "DisableDupeLogDeletes", kind = ArgKind.FLAG, order = 22)
    val disableDupeLogDeletes: Boolean = false,
    @field:ArgOption(name = "ignoredupeditems", kind = ArgKind.FLAG, order = 23)
    val ignoreDupedItems: Boolean = false,
    @Unsure
    @field:ArgOption(name = "noundermeshchecking", kind = ArgKind.FLAG, order = 24)
    val noUnderMeshChecking: Boolean = false,
    @Unsure
    @field:ArgOption(name = "noundermeshkilling", kind = ArgKind.FLAG, order = 25)
    val noUnderMeshKilling: Boolean = false,
    @field:ArgOption(name = "servergamelog", kind = ArgKind.FLAG, order = 26)
    val serverGameLog: Boolean = false,
    @field:ArgOption(name = "servergamelogincludetribelogs", kind = ArgKind.FLAG, order = 27)
    val serverGameLogIncludeTribeLogs: Boolean = false,
    @field:ArgOption(name = "ServerRCONOutputTribeLogs", kind = ArgKind.FLAG, order = 28)
    val serverRconOutputTribeLogs: Boolean = false,
    @field:ArgOption(name = "log", kind = ArgKind.FLAG, order = 29)
    val log: Boolean = false,
    @field:ArgOption(name = "usestore", kind = ArgKind.FLAG, order = 30)
    val useStore: Boolean = false,
    @field:ArgOption(name = "converttostore", kind = ArgKind.FLAG, order = 31)
    val convertToStore: Boolean = false,
    @field:ArgOption(name = "NoWildBabies", kind = ArgKind.FLAG, order = 32)
    val noWildBabies: Boolean = false,
    @field:ArgOption(name = "NoAI", kind = ArgKind.FLAG, order = 33)
    val noAi: Boolean = false,
    @field:ArgOption(name = "nosound", kind = ArgKind.FLAG, order = 34)
    val noSound: Boolean = false,
    @field:ArgOption(name = "onethread", kind = ArgKind.FLAG, order = 35)
    val oneThread: Boolean = false,
    @field:ArgOption(name = "noperfthreads", kind = ArgKind.FLAG, order = 36)
    val noPerfThreads: Boolean = false,
    @field:ArgOption(name = "forceuseperfthreads", kind = ArgKind.FLAG, order = 37)
    val forceUsePerfThreads: Boolean = false,
    @field:ArgOption(name = "disabledinonetrangescaling", kind = ArgKind.FLAG, order = 38)
    val disableInOneRangeScaling: Boolean = false,
    @field:ArgOption(name = "AlwaysTickDedicatedSkeletalMeshes", kind = ArgKind.FLAG, order = 39)
    val alwaysTickDedicatedSkeletalMeshes: Boolean = false,
    @Unsure
    @field:ArgOption(name = "NoHangDetection", kind = ArgKind.FLAG, order = 40)
    val noHangDetection: Boolean = false,
    @Unsure
    @field:ArgOption(name = "PreventHibernation", kind = ArgKind.FLAG, order = 41)
    val preventHibernation: Boolean = false,
    @field:ArgOption(name = "NoDinosExceptForcedSpawn", kind = ArgKind.FLAG, order = 42)
    val noDinosExceptForcedSpawn: Boolean = false,
    @field:ArgOption(name = "NoDinosExceptStreamingSpawn", kind = ArgKind.FLAG, order = 43)
    val noDinosExceptStreamingSpawn: Boolean = false,
    @field:ArgOption(name = "NoDinosExceptManualSpawn", kind = ArgKind.FLAG, order = 44)
    val noDinosExceptManualSpawn: Boolean = false,
    @field:ArgOption(name = "NoDinosExceptWaterSpawn", kind = ArgKind.FLAG, order = 45)
    val noDinosExceptWaterSpawn: Boolean = false,
    @field:ArgOption(name = "NitradoQueryPort", kind = ArgKind.FLAG, order = 46)
    val nitradoQueryPort: Boolean = false,
    @field:ArgOption(name = "MULTIHOME", kind = ArgKind.FLAG, order = 47)
    val multiHome: Boolean = false,
    @field:ArgOption(name = "StasisKeepControllers", kind = ArgKind.FLAG, order = 48)
    val stasisKeepControllers: Boolean = false,
    @field:ArgOption(name = "passivemods", kind = ArgKind.LIST, order = 49)
    val passiveMods: List<String> = emptyList(),
    @field:ArgOption(name = "ForceAllowCaveFlyers", kind = ArgKind.FLAG, order = 50)
    val forceAllowCaveFlyers: Boolean = false,
) {
    fun validate() = true

    fun getEnabledOptions(): List<String> = buildStartupArguments(this)

    companion object {
        internal fun fromParsedStartupScript(parsed: ParsedStartupScript): Options {
            return parseByArgOptions(parsed)
        }
    }
}
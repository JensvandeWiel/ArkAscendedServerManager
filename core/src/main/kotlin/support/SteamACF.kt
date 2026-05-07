package support

import java.io.File

object AcfParser {

    fun parse(input: String): Map<String, Any> {
        val tokens = tokenize(input).iterator()
        return parseObject(tokens)
    }

    private fun tokenize(input: String): List<String> {
        val regex = """\"((?:\\.|[^\"\\])*)\"|([{}])""".toRegex()

        return regex.findAll(input).map { match ->
            match.groups[1]?.value
                ?.replace("\\\"", "\"")
                ?.replace("\\\\", "\\")
                ?: match.groups[2]?.value!!
        }.toList()
    }
    private fun parseObject(tokens: Iterator<String>): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        var currentKey: String? = null

        while (tokens.hasNext()) {
            val token = tokens.next()

            when (token) {
                "}" -> return map
                "{" -> {
                    if (currentKey != null) {
                        map[currentKey] = parseObject(tokens)
                        currentKey = null
                    } else {
                        throw IllegalArgumentException("Malformed ACF: Found '{' without a preceding key.")
                    }
                }
                else -> {
                    if (currentKey == null) {
                        currentKey = token
                    } else {
                        map[currentKey] = token
                        currentKey = null
                    }
                }
            }
        }
        return map
    }
}
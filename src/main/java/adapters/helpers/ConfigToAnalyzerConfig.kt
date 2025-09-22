package adapters.helpers

import adapters.helpers.InputStreamToJson.convert
import checkers.IdentifierStyle
import config.AnalyzerConfig
import java.io.InputStream

class ConfigToAnalyzerConfig {

    companion object {
        fun parseConfigFromString(config: InputStream): AnalyzerConfig {
            val entries = convert(config)

            val identifierStyle = entries["identifier_format"]?.let { format ->
                when (format.lowercase()) {
                    "snake case" -> IdentifierStyle.SNAKE_CASE
                    "camel case" -> IdentifierStyle.CAMEL_CASE
                    else -> IdentifierStyle.NO_STYLE
                }
            } ?: IdentifierStyle.NO_STYLE

            val restrictPrintlnArgs = entries["mandatory-variable-or-literal-in-println"]?.toBoolean()
                ?: false

            val restrictReadInputArgs = entries["mandatory-variable-or-literal-in-readInput"]?.toBoolean()
                ?: false

            return AnalyzerConfig(
                identifierStyle = identifierStyle,
                restrictPrintlnArgs = restrictPrintlnArgs,
                restrictReadInputArgs = restrictReadInputArgs
            )
        }
    }
}
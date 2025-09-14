package adapters

import adapters.InputStreamToJson.convert
import adapters.ParserAdapter.parse
import adapters.VersionAdapter.convertVersion
import config.AnalyzerConfig
import diagnostic.Diagnostic
import factory.AnalyzerFactory
import interpreter.ErrorHandler
import interpreter.PrintScriptLinter
import rules.IdentifierStyle
import java.io.File
import java.io.InputStream

class LinterAdapter: PrintScriptLinter {
    override fun lint(
        src: InputStream,
        version: String,
        config: InputStream,
        handler: ErrorHandler
    ) {
        val analyzerConfig = parseConfigFromString(config.bufferedReader().use { it.readText() })
        val tempConfigFile =
            File.createTempFile("analyzer", ".json").apply {
                writeText(
                    """
                    {
                      "identifierStyle":"${analyzerConfig.identifierStyle}",
                      "restrictPrintlnArgs":${analyzerConfig.restrictPrintlnArgs},
                      "restrictReadInputArgs":${analyzerConfig.restrictReadInputArgs}
                    }
                    """.trimIndent(),
                )
                deleteOnExit()
            }
        val linter = AnalyzerFactory.createWithVersion(convertVersion(version), tempConfigFile.absolutePath)
        val result = linter.analyze(parse(src, version).parse())
        diagnose(result, handler)
    }

    private fun diagnose(diagnostics: List<Diagnostic>, handler: ErrorHandler) {
        for (individualDiagnostic in diagnostics) {
            handler.reportError(individualDiagnostic.message)
        }
    }

    private fun parseConfigFromString(configText: String): AnalyzerConfig {
        val entries = convert(configText)

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
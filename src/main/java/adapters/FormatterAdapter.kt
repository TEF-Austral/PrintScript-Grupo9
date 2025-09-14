package adapters

import adapters.InputStreamToJson.convert
import adapters.ParserAdapter.parse
import adapters.VersionAdapter.convertVersion
import formatter.config.FormatConfig
import formatter.factory.FormatterFactory
import interpreter.PrintScriptFormatter
import java.io.InputStream
import java.io.Writer

class FormatterAdapter: PrintScriptFormatter {
    override fun format(src: InputStream, version: String, config: InputStream, writer: Writer) {
        val program = parse(src, version).parse().getProgram()
        val configText = config.bufferedReader().use { it.readText() }
        val formatConfig = parseConfigFromString(configText)
        FormatterFactory.createWithVersion(convertVersion(version)).formatToWriter(program, formatConfig, writer)
    }

    private fun parseConfigFromString(configText: String): FormatConfig {
        val entries = convert(configText)

        val spaceAroundAssignment = when {
            entries.containsKey("enforce-no-spacing-around-equals") ->
                !entries["enforce-no-spacing-around-equals"]!!.toBoolean()
            entries.containsKey("enforce-spacing-around-equals") ->
                entries["enforce-spacing-around-equals"]!!.toBoolean()
            else -> false
        }

        val spaceAfterColon = entries["enforce-spacing-after-colon-in-declaration"]?.toBoolean() ?: false

        val spaceBeforeColon = entries["enforce-spacing-before-colon-in-declaration"]?.toBoolean() ?: false

        val blankLinesBeforePrintln = entries["line-breaks-after-println"]?.toIntOrNull() ?: 0

        val indentSize = entries["indent-inside-if"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE

        return FormatConfig(
            spaceBeforeColon = spaceBeforeColon,
            spaceAfterColon = spaceAfterColon,
            spaceAroundAssignment = spaceAroundAssignment,
            blankLinesBeforePrintln = blankLinesBeforePrintln,
            indentSize = indentSize
        )
    }
}
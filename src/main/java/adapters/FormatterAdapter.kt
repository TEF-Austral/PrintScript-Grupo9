package adapters

import adapters.InputStreamToJson.convert
import adapters.ParserAdapter.parse
import adapters.VersionAdapter.convertVersion
import formatter.config.FormatConfig
import formatter.factory.FormatterFactory
import formatter.rules.RuleId
import interpreter.PrintScriptFormatter
import java.io.InputStream
import java.io.Writer

class FormatterAdapter : PrintScriptFormatter {
    override fun format(src: InputStream, version: String, config: InputStream, writer: Writer) {
        val program = parse(src, version).parse().getProgram()
        val configText = config.bufferedReader().use { it.readText() }
        val formatConfig = parseConfigFromString(configText)
        FormatterFactory.createWithVersion(convertVersion(version))
            .formatToWriter(program, formatConfig, writer)
    }

    private fun parseConfigFromString(configText: String): FormatConfig {
        val entries = convert(configText)

        // equals spacing: prefer "no-spacing" over "spacing" when both provided
        val spaceAroundAssignment: Boolean? =
            when {
                entries.containsKey("enforce-no-spacing-around-equals") -> {
                    val noSpacing = entries["enforce-no-spacing-around-equals"]!!.toBoolean()
                    if (noSpacing) false else null // false => do not enforce
                }
                entries.containsKey("enforce-spacing-around-equals") -> {
                    val spacing = entries["enforce-spacing-around-equals"]!!.toBoolean()
                    if (spacing) true else null // false => do not enforce
                }
                else -> null
            }

        // colon spacing: presence => enforce given value; absence => do not enforce
        val spaceAfterColon: Boolean? =
            if (entries.containsKey("enforce-spacing-after-colon-in-declaration"))
                entries["enforce-spacing-after-colon-in-declaration"]!!.toBoolean()
            else null

        val spaceBeforeColon: Boolean? =
            if (entries.containsKey("enforce-spacing-before-colon-in-declaration"))
                entries["enforce-spacing-before-colon-in-declaration"]!!.toBoolean()
            else null

        val blankLinesAfterPrintln: Int =
            entries["line-breaks-after-println"]?.toIntOrNull() ?: 0

        // optional extras
        val indentSize: Int =
            entries["indent-size"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE

        val ifIndentInside: Int =
            entries["indent-inside-if"]?.toIntOrNull() ?: FormatConfig.DEFAULT_IF_INDENT_INSIDE

        val ifBraceOnSameLine: Boolean? =
            if (entries.containsKey("if-brace-on-same-line"))
                entries["if-brace-on-same-line"]!!.toBoolean()
            else null

        // enable only rules that have explicit options
        val enabled = mutableSetOf<RuleId>()
        if (spaceBeforeColon != null || spaceAfterColon != null) enabled += RuleId.Declaration
        if (spaceAroundAssignment != null) enabled += RuleId.Assignment
        if (entries.containsKey("line-breaks-after-println")) enabled += RuleId.PrintStatement
        if (entries.containsKey("indent-inside-if") || entries.containsKey("if-brace-on-same-line")) {
            enabled += RuleId.IfStatement
        }

        return FormatConfig(
            spaceBeforeColon = spaceBeforeColon,
            spaceAfterColon = spaceAfterColon,
            spaceAroundAssignment = spaceAroundAssignment,
            blankLinesAfterPrintln = blankLinesAfterPrintln,
            indentSize = indentSize,
            ifBraceOnSameLine = ifBraceOnSameLine,
            ifIndentInside = ifIndentInside,
            enabledRules = enabled
        )
    }
}

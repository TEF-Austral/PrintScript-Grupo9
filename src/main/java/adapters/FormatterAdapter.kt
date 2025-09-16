package adapters

import adapters.InputStreamToJson.convert
import adapters.InputStreamToReader.adaptInputStreamToReader
import adapters.VersionAdapter.convertVersion
import factory.DefaultLexerFactory
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.FormatterImpl
import formatter.config.FormatConfig
import interpreter.PrintScriptFormatter
import stream.token.LexerTokenStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.Writer

class FormatterAdapter : PrintScriptFormatter {
    override fun format(src: InputStream, version: String, config: InputStream, writer: Writer) {
        val configText = config.bufferedReader().use { it.readText() }
        val formatConfig = parseConfigFromString(configText)
        val reader = BufferedReader(adaptInputStreamToReader(src))
        val lexerFactory = DefaultLexerFactory(StringSplitterFactory, StringToTokenConverterFactory)
        val lexer = lexerFactory.createLexerWithVersion( convertVersion(version), reader)
        val tokens = LexerTokenStream(lexer)
        FormatterImpl().formatToWriter(tokens, formatConfig, writer)
    }

    private fun parseConfigFromString(configText: String): FormatConfig {
        val entries = convert(configText)

        val spaceAroundAssignment: Boolean? =
            when {
                entries.containsKey("enforce-no-spacing-around-equals") -> {
                    val noSpacing = entries["enforce-no-spacing-around-equals"]!!.toBoolean()
                    if (noSpacing) false else null
                }

                entries.containsKey("enforce-spacing-around-equals") -> {
                    val spacing = entries["enforce-spacing-around-equals"]!!.toBoolean()
                    if (spacing) true else null
                }

                else -> null
            }

        val spaceAfterColon: Boolean? =
            if (entries.containsKey("enforce-spacing-after-colon-in-declaration"))
                entries["enforce-spacing-after-colon-in-declaration"]!!.toBoolean()
            else null

        val spaceBeforeColon: Boolean? =
            if (entries.containsKey("enforce-spacing-before-colon-in-declaration"))
                entries["enforce-spacing-before-colon-in-declaration"]!!.toBoolean()
            else null

        val blankLinesAfterPrintlnStr = entries["line-breaks-after-println"]
        val blankLinesAfterPrintln = blankLinesAfterPrintlnStr?.toIntOrNull() ?: 0

        val indentSize: Int = entries["indent-inside-if"]?.toIntOrNull() ?: FormatConfig.DEFAULT_SIZE

        val ifBraceOnSameLine: Boolean? =
            if (entries.containsKey("if-brace-below-line"))
                !entries["if-brace-below-line"]!!.toBoolean()
            else null

        val enforceSingleSpace: Boolean? =
            if (entries.containsKey("mandatory-single-space-separation")) {
                entries["mandatory-single-space-separation"]!!.toBoolean()
            }
            else null

        val spaceAroundOperators: Boolean? =
            if (entries.containsKey("mandatory-space-surrounding-operations")) {
                entries["mandatory-space-surrounding-operations"]!!.toBoolean()
            }
            else null

        return FormatConfig(
            spaceBeforeColon = spaceBeforeColon,
            spaceAfterColon = spaceAfterColon,
            spaceAroundAssignment = spaceAroundAssignment,
            blankLinesAfterPrintln = blankLinesAfterPrintln,
            indentSize = indentSize,
            ifBraceOnSameLine = ifBraceOnSameLine,
            enforceSingleSpace = enforceSingleSpace,
            spaceAroundOperators = spaceAroundOperators
        )
    }
}

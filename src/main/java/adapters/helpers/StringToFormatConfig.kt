package adapters.helpers

import formatter.config.FormatConfig
import kotlin.text.toBoolean
import kotlin.text.toIntOrNull

object StringToFormatConfig {
    fun parseConfigFromString(configMap: Map<String, String>): FormatConfig {

        val spaceAroundAssignment: Boolean? =
            when {
                configMap.containsKey("enforce-no-spacing-around-equals") -> {
                    val noSpacing = configMap["enforce-no-spacing-around-equals"]!!.toBoolean()
                    if (noSpacing) false else null
                }

                configMap.containsKey("enforce-spacing-around-equals") -> {
                    val spacing = configMap["enforce-spacing-around-equals"]!!.toBoolean()
                    if (spacing) true else null
                }

                else -> null
            }

        val spaceAfterColon: Boolean? =
            if (configMap.containsKey("enforce-spacing-after-colon-in-declaration"))
                configMap["enforce-spacing-after-colon-in-declaration"]!!.toBoolean()
            else null

        val spaceBeforeColon: Boolean? =
            if (configMap.containsKey("enforce-spacing-before-colon-in-declaration"))
                configMap["enforce-spacing-before-colon-in-declaration"]!!.toBoolean()
            else null

        val blankLinesAfterPrintlnStr = configMap["line-breaks-after-println"]
        val blankLinesAfterPrintln = blankLinesAfterPrintlnStr?.toIntOrNull() ?: 0

        val indentSize: Int = configMap["indent-inside-if"]?.toIntOrNull() ?: FormatConfig.DEFAULT_SIZE

        val ifBraceOnSameLine: Boolean? =
            if (configMap.containsKey("if-brace-below-line")) !configMap["if-brace-below-line"]!!.toBoolean()
            else if (configMap.containsKey("if-brace-same-line")) configMap["if-brace-same-line"]!!.toBoolean()
            else true

        val enforceSingleSpace: Boolean? =
            if (configMap.containsKey("mandatory-single-space-separation")) {
                configMap["mandatory-single-space-separation"]!!.toBoolean()
            }
            else null

        val spaceAroundOperators: Boolean? =
            if (configMap.containsKey("mandatory-space-surrounding-operations")) {
                configMap["mandatory-space-surrounding-operations"]!!.toBoolean()
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
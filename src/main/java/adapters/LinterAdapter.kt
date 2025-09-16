package adapters

import adapters.helpers.ConfigToAnalyzerConfig
import adapters.helpers.ParserAdapter.parse
import adapters.helpers.VersionAdapter.convertVersion
import diagnostic.Diagnostic
import factory.AnalyzerFactory
import interpreter.ErrorHandler
import interpreter.PrintScriptLinter
import java.io.InputStream

class LinterAdapter: PrintScriptLinter {
    override fun lint(src: InputStream, version: String, config: InputStream, handler: ErrorHandler) {
        val analyzerConfig = ConfigToAnalyzerConfig.parseConfigFromString(config)
        val tempConfigFile = ConfigToAnalyzerConfig.convertToTempConfigFile(analyzerConfig)
        val linter = AnalyzerFactory.createWithVersion(convertVersion(version), tempConfigFile.absolutePath)
        val result = linter.analyze(parse(src, version).parse())
        diagnose(result, handler)
    }

    private fun diagnose(diagnostics: List<Diagnostic>, handler: ErrorHandler) {
        for (individualDiagnostic in diagnostics) {
            handler.reportError(individualDiagnostic.message)
        }
    }
}
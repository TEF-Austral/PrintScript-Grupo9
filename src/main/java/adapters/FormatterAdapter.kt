package adapters

import adapters.helpers.InputStreamToJson.convert
import adapters.helpers.InputStreamToReader.adaptInputStreamToReader
import adapters.helpers.StringToFormatConfig.parseConfigFromString
import adapters.helpers.VersionAdapter.convertVersion
import factory.DefaultLexerFactory
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import formatter.factory.DefaultFormatterFactory.createFormatter
import interpreter.PrintScriptFormatter
import stream.token.LexerTokenStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.Writer

class FormatterAdapter : PrintScriptFormatter {
    override fun format(src: InputStream, version: String, config: InputStream, writer: Writer) {
        val reader = BufferedReader(adaptInputStreamToReader(src))
        val lexerFactory = DefaultLexerFactory(StringSplitterFactory, StringToTokenConverterFactory)
        val lexer = lexerFactory.createLexerWithVersion( convertVersion(version), reader)
        val tokens = LexerTokenStream(lexer)
        val formatter = createFormatter(convertVersion(version))
        formatter.formatToWriter(tokens, parseConfigFromString(convert(config)), writer)
    }
}

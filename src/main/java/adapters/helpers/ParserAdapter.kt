package adapters.helpers

import adapters.helpers.InputStreamToReader.adaptInputStreamToReader
import builder.DefaultNodeBuilder
import factory.DefaultLexerFactory
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import parser.ParserInterface
import parser.factory.DefaultParserFactory
import stream.token.LexerTokenStream
import java.io.InputStream
import java.io.BufferedReader

object ParserAdapter {
    fun parse(src: InputStream, version: String): ParserInterface {
        val reader = BufferedReader(adaptInputStreamToReader(src))
        val lexerFactory = DefaultLexerFactory(StringSplitterFactory, StringToTokenConverterFactory)
        val lexer = lexerFactory.createLexerWithVersion(VersionAdapter.convertVersion(version), reader)
        val tokens = LexerTokenStream(lexer)
        return DefaultParserFactory.createWithVersion(VersionAdapter.convertVersion(version), DefaultNodeBuilder(), tokens)
    }
}
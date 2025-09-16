package adapters

import adapters.InputStreamToReader.adaptInputStreamToReader
import adapters.VersionAdapter.convertVersion
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
        val lexer = lexerFactory.createLexerWithVersion( convertVersion(version), reader)
        val tokens = LexerTokenStream(lexer)
        return DefaultParserFactory().createWithVersion(convertVersion(version), DefaultNodeBuilder(), tokens)
    }
}
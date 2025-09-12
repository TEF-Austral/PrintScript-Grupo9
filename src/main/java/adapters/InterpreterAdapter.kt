package adapters

import MockReader
import adapters.VersionAdapter.convertVersion
import builder.DefaultNodeBuilder
import factory.DefaultInterpreterFactory
import factory.DefaultLexerFactory
import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import parser.factory.DefaultParserFactory
import java.io.InputStream

class InterpreterAdapter : PrintScriptInterpreter {

    override fun execute(src: InputStream, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val lexer = DefaultLexerFactory(StringSplitterFactory, StringToTokenConverterFactory).createLexerWithVersion(convertVersion(version))
        val content = src.bufferedReader().use { it.readText() }
        val tokens = lexer.tokenize(MockReader(content))
        val parser = DefaultParserFactory().createWithVersion(convertVersion(version), DefaultNodeBuilder(), tokens)
        val parserResult = parser.parse()
        val interpreter = DefaultInterpreterFactory().createWithVersionAndEmitterAndInputProvider(convertVersion(version), PrintEmitterAdapter(emitter), InputProviderAdapter(provider, emitter))
        val result = interpreter.interpret(parserResult.getProgram())
        if (!result.interpretedCorrectly) {
            handler.reportError("")
        }
    }
}
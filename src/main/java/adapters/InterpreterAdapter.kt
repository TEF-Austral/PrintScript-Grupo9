package adapters

import adapters.ParserAdapter.parse
import adapters.VersionAdapter.convertVersion
import factory.DefaultInterpreterFactory
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import parser.stream.ParserAstStream
import java.io.InputStream

class InterpreterAdapter : PrintScriptInterpreter {

    override fun execute(src: InputStream, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val astStream = ParserAstStream(parse(src, version))
        val interpreter = DefaultInterpreterFactory().createWithVersionAndEmitterAndInputProvider(convertVersion(version), PrintEmitterAdapter(emitter), InputProviderAdapter(provider, emitter))
        val result = interpreter.interpret(astStream)
        if (!result.interpretedCorrectly) {
            handler.reportError(result.message)
        }
    }
}
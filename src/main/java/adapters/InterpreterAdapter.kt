package adapters

import adapters.helpers.ParserAdapter.parse
import adapters.helpers.InputProviderAdapter
import adapters.helpers.VersionAdapter.convertVersion
import adapters.helpers.PrintEmitterAdapter
import factory.DefaultInterpreterFactory.createWithVersionAndEmitterAndInputProvider
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import parser.stream.ParserAstStream
import java.io.InputStream

class InterpreterAdapter : PrintScriptInterpreter {

    override fun execute(src: InputStream, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val astStream = ParserAstStream(parse(src, version))
        val adaptedEmitter = PrintEmitterAdapter(emitter)
        val adaptedInput = InputProviderAdapter(provider, emitter)
        val interpreter = createWithVersionAndEmitterAndInputProvider(convertVersion(version), adaptedEmitter, adaptedInput)
        val result = interpreter.interpret(astStream)
        if (!result.interpretedCorrectly) {
            handler.reportError(result.message)
        }
    }
}
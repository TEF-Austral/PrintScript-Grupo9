package adapters

import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import java.io.InputStream

class InterpreterAdapter: PrintScriptInterpreter {
    override fun execute(
        src: InputStream?,
        version: String?,
        emitter: PrintEmitter?,
        handler: ErrorHandler?,
        provider: InputProvider?
    ) {
        TODO("Not yet implemented")
    }
}
package adapters

import CLI
import flags.CliFlags
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import java.io.InputStream

class CLIAdapter : PrintScriptInterpreter {

    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider
    ) {
        val result = CLI().execute(CliFlags.EXECUTION,
            "",
            null,
            null,
            version)
        EmitterAdapter().print(result)
    }
}
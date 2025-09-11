package adapters

import interpreter.ErrorHandler
import interpreter.PrintScriptLinter
import java.io.InputStream

class LinterAdapter: PrintScriptLinter {
    override fun lint(
        src: InputStream,
        version: String,
        config: InputStream,
        handler: ErrorHandler
    ) {
        TODO("Not yet implemented")
    }
}
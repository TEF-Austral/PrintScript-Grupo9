package adapters

import formatter.FormatterService
import interpreter.PrintScriptFormatter
import java.io.InputStream
import java.io.Writer

class FormatterAdapter: PrintScriptFormatter {
    override fun format(
        src: InputStream,
        version: String,
        config: InputStream,
        writer: Writer
    ) {
        FormatterService().formatToWriter()
    }

    private fun getParser():
}
package adapters

import adapters.VersionAdapter.convertVersion
import formatter.factory.FormatterFactory
import interpreter.PrintScriptFormatter
import java.io.InputStream
import java.io.Writer

class FormatterAdapter: PrintScriptFormatter {
    override fun format(src: InputStream, version: String, config: InputStream, writer: Writer) {
        FormatterFactory.createWithVersion(convertVersion(version))
    }
}
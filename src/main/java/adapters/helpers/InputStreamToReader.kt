package adapters.helpers

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.StandardCharsets

object InputStreamToReader {
    fun adaptInputStreamToReader(src: InputStream): Reader {
        return InputStreamReader(src, StandardCharsets.UTF_8)
    }
}

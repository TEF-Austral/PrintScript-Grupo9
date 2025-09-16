package adapters.helpers

import java.io.InputStream

object InputStreamToJson {
    fun convert(configText: InputStream): Map<String, String> {
        val config = configText.bufferedReader().use { it.readText() }
        val configToJson =  config
            .trim()
            .removePrefix("{")
            .removeSuffix("}")
            .split(',')
            .map { it.trim().split(':', limit = 2).map(String::trim) }
            .filter { it.size == 2 }
            .associate { it[0].removeSurrounding("\"") to it[1].removeSurrounding("\"") }
        return configToJson
    }
}
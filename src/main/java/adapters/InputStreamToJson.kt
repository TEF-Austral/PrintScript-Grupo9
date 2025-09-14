package adapters

object InputStreamToJson {
    fun convert(configText: String): Map<String, String> {
        return configText
            .trim()
            .removePrefix("{")
            .removeSuffix("}")
            .split(',')
            .map { it.trim().split(':', limit = 2).map(String::trim) }
            .filter { it.size == 2 }
            .associate { it[0].removeSurrounding("\"") to it[1].removeSurrounding("\"") }
    }
}
package adapters.helpers

import transformer.StringToPrintScriptVersion
import type.Version

object VersionAdapter {
    fun convertVersion(version: String): Version {
        return StringToPrintScriptVersion().transform(version)
    }
}
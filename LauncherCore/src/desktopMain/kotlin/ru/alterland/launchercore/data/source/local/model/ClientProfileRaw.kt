package ru.alterland.launchercore.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ClientProfileRaw(
    @SerialName("id") val id: String?,
    @SerialName("configVersion") val configVersion: Int? = null,
    @SerialName("mainClass") val mainClass: String? = null,
    @SerialName("arguments") val arguments: Arguments? = null,
    @SerialName("downloads") val downloads: List<DownloadIndex>? = null,
    @SerialName("externals") val externals: List<ExternalIndex>? = null,
    @SerialName("modules") val modules: List<String>? = null,
    @SerialName("strict") val strict: List<String>? = null
) {

    @Serializable
    data class Arguments(
        val game: List<JsonElement>?,
        val jvm: List<JsonElement>?
    )

    @Serializable
    data class Argument(
        val rules: List<Rule>?,
        val value: JsonElement
    )

    @Serializable
    data class Rule(
        val action: String?,
        val os: OS?,
        val features: Map<String, Boolean>?
    )

    @Serializable
    data class OS(
        val name: String?,
        val arch: String?,
        val version: String?
    )

    @Serializable
    data class ExternalIndex(
        val indexPath: String?,
        val externalsPath: String?,
        val checkSum: String?,
        val url: String?,
        val type: String?,
        val rules: List<Rule>?
    )

    @Serializable
    data class DownloadIndex(
        val path: String?,
        val checkSum: String?,
        val size: Long?,
        val url: String?,
        val classPath: Boolean?,
        val allowChanges: Boolean?,
        val rules: List<Rule>?
    )
}

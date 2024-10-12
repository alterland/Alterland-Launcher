package ru.alterland.launchercore.domain.model

import ru.alterland.launchercore.domain.model.externalindex.ExternalIndexType

data class ClientProfile(
    val id: String,
    val configVersion: Int,
    val mainClass: String,
    val gameArguments: List<Argument>,
    val jvmArguments: List<Argument>,
    val downloads: List<DownloadIndex>,
    val externals: List<ExternalIndex>,
    val modules: List<String>,
    val strict: List<String>,
    val status: ClientStatus
) {

    data class Argument(
        val rules: List<Rule>,
        val value: List<String>
    )

    data class ExternalIndex(
        val indexPath: String,
        val externalsPath: String,
        val checkSum: String,
        val url: String,
        val type: ExternalIndexType,
        val rules: List<Rule>
    )

    data class DownloadIndex(
        val path: String,
        val checkSum: String,
        val size: Long,
        val url: String,
        val classPath: Boolean,
        val rules: List<Rule>
    )

    data class Rule(
        val action: ActionRule?,
        val os: OS?,
        val features: Map<String, Boolean>
    )
}

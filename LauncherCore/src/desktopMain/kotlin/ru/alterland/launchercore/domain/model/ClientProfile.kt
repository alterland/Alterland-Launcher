package ru.alterland.launchercore.domain.model

import kotlinx.serialization.Serializable

data class ClientProfile(
    val id: String,
    val gameArguments: List<Argument>,
    val jvmArguments: List<Argument>,
    val assets: Assets?,
    val libraries: List<Library>,
    val extra: List<Library>,
    val mainClass: String?,
    val javaVersion: JavaVersion?,
    val type: String?,
    val modules: List<String>,
    val strict: List<String>,
    val status: ClientStatus
) {
    @Serializable
    data class Argument(
        val rules: List<Rule>,
        val value: List<String>
    )

    @Serializable
    data class Rule(
        val action: ActionRule?,
        val os: OS?,
        val features: Map<String, Boolean>
    )

    data class Assets(
        val id: String,
        val checkSum: String,
        val size: Long,
        val totalSize: Long,
        val url: String
    )

    data class Artifact(
        val path: String,
        val checkSum: String,
        val size: Long,
        val url: String
    )

    data class Downloads(
        val artifact: Artifact? = null,
        val classifiers: Map<String, Artifact> = mapOf()
    )

    data class Library(
        val name: String? = null,
        val downloads: Downloads? = null,
        val rules: List<Rule> = listOf(),
        val natives: Natives? = null
    )

    data class Natives(
        val windows: String?,
        val osx: String?,
        val linux: String?
    )

    data class JavaVersion(
        val component: String,
        val majorVersion: Int
    )
}

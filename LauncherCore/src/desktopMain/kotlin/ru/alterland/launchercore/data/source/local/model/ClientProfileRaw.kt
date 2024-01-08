package ru.alterland.launchercore.data.source.local.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientProfileRaw(
    val id: String?,
    val gameArguments: List<Argument>?,
    val jvmArguments: List<Argument>?,
    val assets: Assets?,
    val libraries: List<Library>?,
    val extra: List<Library>?,
    val mainClass: String?,
    val javaVersion: JavaVersion?,
    val modules: List<Module>?
) {
    @Serializable
    data class Argument(
        val rules: List<Rule>?,
        val value: List<String>?
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
    data class Assets(
        val id: String?,
        val checkSum: String?,
        val size: Long?,
        val totalSize: Long?,
        val url: String?
    )

    @Serializable
    data class Artifact(
        val path: String?,
        val checkSum: String?,
        val size: Long?,
        val url: String?
    )

    @Serializable
    data class Downloads(
        val artifact: Artifact?,
        val classifiers: Map<String, Artifact>?
    )

    @Serializable
    data class Library(
        val name: String?,
        val downloads: Downloads?,
        val rules: List<Rule>?,
        val natives: Natives?
    )

    @Serializable
    data class Natives(
        val windows: String?,
        val osx: String?,
        val linux: String?
    )

    @Serializable
    data class JavaVersion(
        val component: String?,
        val majorVersion: Int?
    )

    @Serializable
    data class Module(
        val type: String?,
        val name: String?
    )
}

package ru.alterland.launchercore.domain.model

import kotlinx.serialization.Serializable

enum class OsName(val value: String, val aliases: List<String>) {
    WINDOWS("windows", listOf("windows", "win")),
    OSX("osx", listOf("mac", "osx")),
    LINUX("linux", listOf("linux", "unix")),
    UNKNOWN("unknown", listOf());

    companion object {
        fun getOsType(os: String): OsName = OsName.entries.firstOrNull {
            it.aliases.any { alias -> os.contains(alias, true) }
        } ?: UNKNOWN
    }
}

enum class OsArch(val value: String, val aliases: List<String>) {
    X86("x86", listOf("x86", "x86_64", "amd64")),
    ARM("arm", listOf("arm", "aarch64", "aarch32")),
    UNKNOWN("unknown", listOf());

    companion object {
        fun getOsArchType(arch: String): OsArch = OsArch.entries.firstOrNull {
            it.aliases.any { alias -> arch.contains(alias, true) }
        } ?: UNKNOWN
    }
}

@Serializable
data class OS(
    val name: OsName?,
    val arch: OsArch?,
    val version: String?
)

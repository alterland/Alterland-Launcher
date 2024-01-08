package ru.alterland.launchercore.domain.model

import kotlinx.serialization.Serializable

enum class OsName(val value: String) {
    WINDOWS("windows"), OSX("osx"), LINUX("linux"), UNKNOWN("unknown");

    companion object {
        fun getOsType(os: String): OsName = OsName.entries.firstOrNull {
            it.value.startsWith(os.lowercase())
        } ?: UNKNOWN
    }
}

enum class OsArch(val value: String) {
    X86("x86"), ARM("arm"), UNKNOWN("unknown");

    companion object {
        fun getOsArchType(arch: String): OsArch = OsArch.entries.firstOrNull {
            it.value.startsWith(arch.lowercase())
        } ?: UNKNOWN
    }
}

@Serializable
data class OS(
    val name: OsName?,
    val arch: OsArch?,
    val version: String?
)

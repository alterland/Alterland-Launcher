package ru.alterland.launcher.util

enum class OS(val value: String) {
    WINDOWS("windows"), LINUX("linux"), MACOSX("macosx"), UNKNOWN("unknown");

    companion object {
        fun fromValue(name: String?): OS = entries.firstOrNull {
            it.value.startsWith(name?.lowercase() ?: "")
        } ?: UNKNOWN
    }
}

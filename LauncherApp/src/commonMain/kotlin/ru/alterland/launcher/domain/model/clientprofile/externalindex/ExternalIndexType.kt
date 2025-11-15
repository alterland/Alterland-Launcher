package ru.alterland.launcher.domain.model.clientprofile.externalindex

enum class ExternalIndexType(val value: String) {
    ASSETS("assets"), DEFAULT("default");

    companion object {
        fun fromValue(name: String?): ExternalIndexType = entries.firstOrNull {
            it.value.startsWith(name?.lowercase() ?: "")
        } ?: DEFAULT
    }
}


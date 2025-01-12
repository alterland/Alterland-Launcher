package ru.alterland.launcher.domain.model.clientprofile.externalindex

import ru.alterland.launcher.domain.model.clientprofile.externalindex.ExternalIndexType.entries


enum class ExternalIndexType(val value: String) {
    ASSETS("assets"), DEFAULT("default");

    companion object {
        fun fromValue(name: String?): ExternalIndexType = entries.firstOrNull {
            it.value.startsWith(name?.lowercase() ?: "")
        } ?: DEFAULT
    }
}


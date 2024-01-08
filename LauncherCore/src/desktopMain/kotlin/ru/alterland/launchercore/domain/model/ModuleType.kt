package ru.alterland.launchercore.domain.model

enum class ModuleType(val value: String) {
    CUSTOM("custom"), MOJANG("mojang");

    companion object {
        fun getModuleType(type: String?): ModuleType = ModuleType.entries.firstOrNull {
            it.value.startsWith(type?.lowercase() ?: "")
        } ?: CUSTOM
    }
}
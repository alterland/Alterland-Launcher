package ru.alterland.launcher.domain.model.clientprofile

enum class ModuleType(val value: String) {
    CUSTOM("custom"), MOJANG("mojang");

    companion object {
        fun getModuleType(type: String?): ModuleType = entries.firstOrNull {
            it.value.startsWith(type?.lowercase() ?: "")
        } ?: CUSTOM
    }
}

package ru.alterland.launcher.domain.model

enum class Role(val value: String) {
    USER("user"), ADMIN("admin");

    companion object {
        fun fromValue(value: String?): Role? = Role.entries.firstOrNull {
            it.value == value
        }
    }
}

package ru.alterland.launcher.domain.model.clientprofile

import ru.alterland.launcher.domain.model.clientprofile.ActionRule.entries


enum class ActionRule(val value: String) {
    ALLOW("allow"), DISALLOW("disallow");

    companion object {
        fun getRule(rule: String): ActionRule = entries.firstOrNull {
            it.value.startsWith(rule.lowercase())
        } ?: ALLOW
    }
}

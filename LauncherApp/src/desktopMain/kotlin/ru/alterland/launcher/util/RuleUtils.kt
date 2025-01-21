package ru.alterland.launcher.util

import ru.alterland.launcher.domain.model.clientprofile.ClientProfile.Rule
import ru.alterland.launcher.domain.model.clientprofile.OS

object ClientProfileUtils {

    @JvmStatic
    fun testRules(
        rules: List<Rule>,
        enabledFeatures: Map<String, Boolean>? = null
    ): Boolean {
        val userOs = OS(name = OS_NAME, arch = OS_ARCH, version = OS_VERSION)
        var testPass = true
        for (rule in rules) {
            testPass = rule.test(userOs, enabledFeatures)
        }
        return testPass
    }
}

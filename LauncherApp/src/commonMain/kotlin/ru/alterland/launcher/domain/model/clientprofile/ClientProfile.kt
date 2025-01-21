package ru.alterland.launcher.domain.model.clientprofile

import ru.alterland.launcher.domain.model.clientprofile.externalindex.ExternalIndexType

data class ClientProfile(
    val id: String,
    val configVersion: Int,
    val mainClass: String,
    val gameArguments: List<Argument>,
    val jvmArguments: List<Argument>,
    val downloads: List<DownloadIndex>,
    val externals: List<ExternalIndex>,
    val modules: List<String>,
    val strict: List<String>,
    val status: ClientStatus
) {

    data class Argument(
        val rules: List<Rule>,
        val value: List<String>
    )

    data class ExternalIndex(
        val indexPath: String,
        val externalsPath: String,
        val checkSum: String,
        val url: String,
        val type: ExternalIndexType,
        val rules: List<Rule>
    )

    data class DownloadIndex(
        val path: String,
        val checkSum: String,
        val size: Long,
        val url: String,
        val classPath: Boolean,
        val allowChanges: Boolean,
        val rules: List<Rule>
    )

    data class Rule(
        val action: ActionRule?,
        val os: OS?,
        val features: Map<String, Boolean>
    ) {

        fun test(
            userOS: OS,
            enabledFeatures: Map<String, Boolean>? = null
        ): Boolean {
            var testPass = true

            if (action == null) return true

            if (os != null) {
                val osNameMatch = if (os.name != null && userOS.name != null) {
                    userOS.name == os.name
                } else true

                val osArchMatch = if (os.arch != null && userOS.arch != null) {
                    userOS.arch == os.arch
                } else true

                val osVersionMatch = if (os.version != null && userOS.version != null) {
                    Regex(os.version).containsMatchIn(userOS.version)
                } else true

                testPass = when(action) {
                    ActionRule.ALLOW -> osNameMatch && osArchMatch && osVersionMatch
                    ActionRule.DISALLOW -> !osNameMatch || !osArchMatch || !osVersionMatch
                }
            }

            if (enabledFeatures != null) {
                features.forEach { feature ->
                    testPass = testPass && feature.value == (enabledFeatures[feature.key] == true)
                }
            }

            return testPass
        }
    }
}

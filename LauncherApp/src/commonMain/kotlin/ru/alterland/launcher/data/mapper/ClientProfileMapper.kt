package ru.alterland.launcher.data.mapper

import kotlinx.serialization.json.*
import ru.alterland.launcher.data.source.network.model.response.ClientProfileResponse
import ru.alterland.launcher.domain.model.clientprofile.*
import ru.alterland.launcher.domain.model.clientprofile.externalindex.ExternalIndexType
import ru.alterland.launcher.util.extentions.v


fun ClientProfileResponse.toDomain(json: Json) = ClientProfile(
    id = id.orEmpty(),
    configVersion = configVersion ?: -1,
    mainClass = mainClass.orEmpty(),
    gameArguments = arguments?.game?.map { it.getArgument(json) } ?: listOf(),
    jvmArguments = arguments?.jvm?.map { it.getArgument(json) } ?: listOf(),
    downloads = downloads?.mapNotNull { it.toDomain() } ?: listOf(),
    externals = externals?.mapNotNull { it.getExternalIndex() } ?: listOf(),
    modules = modules ?: listOf(),
    strict = strict ?: listOf(),
    status = ClientStatus.Unknown
)

private fun JsonElement.getArgument(json: Json) = when(this) {
    is JsonPrimitive -> ClientProfile.Argument(
        value = listOf(this.jsonPrimitive.content),
        rules = listOf()
    )
    is JsonObject -> json.decodeFromJsonElement<ClientProfileResponse.Argument>(this).getArgument()
    else -> throw Exception("cannot decode $this")
}

private fun ClientProfileResponse.Argument.getArgument() = ClientProfile.Argument(
    rules = rules?.map { it.getRule() } ?: listOf(),
    value = when(value) {
        is JsonPrimitive -> listOf(value.jsonPrimitive.content)
        is JsonArray -> value.map { it.jsonPrimitive.content }
        else -> listOf()
    }
)

private fun ClientProfileResponse.Rule.getRule() = ClientProfile.Rule(
    action = action?.let { ActionRule.getRule(it) },
    os = os?.getOS(),
    features = features ?: mapOf()
)

private fun ClientProfileResponse.OS.getOS() = OS(
    name = name?.let { OsName.getOsType(it) },
    arch = arch?.let { OsArch.getOsArchType(it) },
    version = version
)

fun ClientProfileResponse.DownloadIndex.toDomain(basePath: String = ""): ClientProfile.DownloadIndex? =
    if (path != null && checkSum != null  && size != null && url != null) {
        ClientProfile.DownloadIndex(
            path = basePath v path,
            checkSum = checkSum,
            size = size,
            url = url,
            classPath = classPath ?: false,
            allowChanges = allowChanges ?: false,
            rules = rules?.map { it.getRule() } ?: listOf()
        )
    } else {
        null
    }

private fun ClientProfileResponse.ExternalIndex.getExternalIndex(): ClientProfile.ExternalIndex? =
    if (indexPath != null && externalsPath != null && checkSum != null  && url != null) {
        ClientProfile.ExternalIndex(
            indexPath = indexPath,
            externalsPath = externalsPath,
            checkSum = checkSum,
            url = url,
            type = ExternalIndexType.fromValue(type),
            rules = rules?.map { it.getRule() } ?: listOf()
        )
    } else {
        null
    }

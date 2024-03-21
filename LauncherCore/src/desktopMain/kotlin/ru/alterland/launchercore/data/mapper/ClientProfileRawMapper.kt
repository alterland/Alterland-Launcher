package ru.alterland.launchercore.data.mapper

import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw
import ru.alterland.launchercore.domain.model.*

fun ClientProfileRaw.toDomain() = ClientProfile(
    id = id.orEmpty(),
    gameArguments = gameArguments?.map { it.getArgument() } ?: listOf(),
    jvmArguments = jvmArguments?.map { it.getArgument() } ?: listOf(),
    assets = assets?.getAssets(),
    libraries = libraries?.map { it.getLibrary() } ?: listOf(),
    extra = extra?.map { it.getLibrary() } ?: listOf(),
    mainClass = mainClass,
    javaVersion = null,
    modules = modules?.filter { it.isNotEmpty() } ?: listOf(),
    type = type,
    strict = strict ?: listOf(),
    status = ClientStatus.Unknown
)

private fun ClientProfileRaw.Argument.getArgument() = ClientProfile.Argument(
    rules = rules?.map { it.getRule() } ?: listOf(),
    value = value ?: listOf()
)

private fun ClientProfileRaw.Rule.getRule() = ClientProfile.Rule(
    action = action?.let { ActionRule.getRule(it) },
    os = os?.getOS(),
    features = features ?: mapOf()
)

private fun ClientProfileRaw.OS.getOS() = OS(
    name = name?.let { OsName.getOsType(it) },
    arch = arch?.let { OsArch.getOsArchType(it) },
    version = version
)

private fun ClientProfileRaw.Library.getLibrary() = ClientProfile.Library(
    name = name.orEmpty(),
    downloads = downloads?.getDownloads(),
    rules = rules?.map { it.getRule() } ?: listOf(),
    natives = natives?.getNatives()
)

private fun ClientProfileRaw.Assets.getAssets() = ClientProfile.Assets (
    id = id.orEmpty(),
    checkSum = checkSum.orEmpty(),
    size = size ?: 0,
    totalSize = totalSize ?: 0,
    url = url.orEmpty(),
)

private fun ClientProfileRaw.Downloads.getDownloads() = ClientProfile.Downloads(
    artifact = artifact?.getArtifact(),
    classifiers = classifiers?.mapValues { it.value.getArtifact() } ?: mapOf()
)

private fun ClientProfileRaw.Artifact.getArtifact() = ClientProfile.Artifact(
    path = path.orEmpty(),
    checkSum = checkSum.orEmpty(),
    size = size ?: 0,
    url = url.orEmpty(),
)

private fun ClientProfileRaw.Natives.getNatives() = ClientProfile.Natives(
    windows = windows,
    osx = osx,
    linux = linux
)

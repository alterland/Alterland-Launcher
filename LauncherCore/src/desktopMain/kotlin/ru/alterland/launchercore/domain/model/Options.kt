package ru.alterland.launchercore.domain.model

data class Options(
    val serverProfile: ServerProfile,
    val player: Player,
    val features: Map<Feature, Boolean> = mapOf()
)

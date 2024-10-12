package ru.alterland.launchercore.domain.model

data class Options(
    val clientProfile: ClientProfile,
    val player: Player,
    val features: Map<Feature, Boolean> = mapOf()
)

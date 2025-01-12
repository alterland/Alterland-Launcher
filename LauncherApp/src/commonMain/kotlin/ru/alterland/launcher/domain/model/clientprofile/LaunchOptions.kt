package ru.alterland.launcher.domain.model.clientprofile

data class LaunchOptions(
    val clientProfile: ClientProfile,
    val player: Player,
    val features: Map<Feature, Boolean> = mapOf()
)

package ru.alterland.launcher.ui.screen.main.skins

import kotlinx.serialization.Serializable
import ru.alterland.launcher.domain.model.ServerProfile

@Serializable
data class SkinsPayload(
    val serverProfile: ServerProfile
)

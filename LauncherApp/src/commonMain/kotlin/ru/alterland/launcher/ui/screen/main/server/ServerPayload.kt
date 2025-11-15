package ru.alterland.launcher.ui.screen.main.server

import kotlinx.serialization.Serializable
import ru.alterland.launcher.domain.model.ServerProfile

@Serializable
data class ServerPayload(
    val serverProfile: ServerProfile
)

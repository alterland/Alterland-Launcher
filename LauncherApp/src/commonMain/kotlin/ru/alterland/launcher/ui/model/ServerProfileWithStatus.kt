package ru.alterland.launcher.ui.model

import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.model.ServerProfile

data class ServerProfileWithStatus(
    val serverProfile: ServerProfile,
    val status: MinecraftServerStatus = MinecraftServerStatus.Polling
)

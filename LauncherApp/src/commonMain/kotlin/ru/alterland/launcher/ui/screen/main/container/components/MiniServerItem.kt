package ru.alterland.launcher.ui.screen.main.container.components

import ru.alterland.launcher.domain.model.MinecraftServerStatus

data class MiniServerItem(
    val id: String,
    val name: String,
    val serverStatus: MinecraftServerStatus
)

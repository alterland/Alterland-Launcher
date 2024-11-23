package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launchercore.domain.model.MinecraftServerStatus

data class MenuItem(
    val name: String,
    val serverStatus: MinecraftServerStatus
)

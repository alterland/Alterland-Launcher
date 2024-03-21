package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launchercore.domain.model.ServerStatus

data class MenuItem(
    val name: String,
    val serverStatus: ServerStatus,
    val ping: Long,
    val max: Int,
    val online: Int,
    val favicon: ByteArray? = null
)

package ru.alterland.launchercore.domain.model

data class ServerPong(
    val serverStatus: ServerStatus = ServerStatus.POLLING,
    val ping: Long = -1,
    val max: Int = -1,
    val online: Int = -1,
    val favicon: ByteArray? = null
)

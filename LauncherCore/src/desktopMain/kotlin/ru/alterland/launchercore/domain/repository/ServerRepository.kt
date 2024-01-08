package ru.alterland.launchercore.domain.repository

import ru.alterland.launchercore.domain.model.PingOptions
import ru.alterland.launchercore.domain.model.PingOptions.Companion.DEFAULT_PORT
import ru.alterland.launchercore.domain.model.ServerPong

interface ServerRepository {
    fun ping(hostname: String, port: Int? = DEFAULT_PORT): ServerPong
    fun ping(options: PingOptions): ServerPong
}

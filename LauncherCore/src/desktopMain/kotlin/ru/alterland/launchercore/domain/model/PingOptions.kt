package ru.alterland.launchercore.domain.model

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class PingOptions(
    val hostname: String,
    val port: Int = DEFAULT_PORT,
    val charset: Charset = StandardCharsets.UTF_8,
    val timeout: Int = 5000,
    val protocolVersion: Int = 4
) {
    companion object {
        const val DEFAULT_PORT = 25565
    }
}

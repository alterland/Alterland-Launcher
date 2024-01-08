package ru.alterland.launchercore.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerAddress(
    val ip: String,
    val port: Int
) {
    fun getAddress() = "${this.ip}:${this.port}"
}

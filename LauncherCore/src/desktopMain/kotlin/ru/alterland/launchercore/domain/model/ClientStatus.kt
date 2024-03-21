package ru.alterland.launchercore.domain.model

sealed class ClientStatus {
    data object Ready: ClientStatus()
    data object Verification: ClientStatus()
    data object Launching: ClientStatus()
    data object Launched: ClientStatus()
    data class Downloading(
        val timeLeft: Double = 0.0,
        val speed: Double = 0.0,
        val received: Long = 0,
        val total: Long = 0
    ): ClientStatus()
    data object DownloadError: ClientStatus()
    data object DownloadPaused: ClientStatus()
    data object UpdateRequired: ClientStatus()
    data object Unknown: ClientStatus()
}

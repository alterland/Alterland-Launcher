package ru.alterland.launchercore.domain.model

sealed class ClientStatus {
    data object Ready: ClientStatus()
    data object Verification: ClientStatus()
    data object Launching: ClientStatus()
    data object Launched: ClientStatus()
    data class Updating(
        val received: Long = 0,
        val total: Long = 0,
        val isPaused: Boolean = false
    ): ClientStatus()
    data class UpdateError(val errorCount: Int): ClientStatus()
    data object UpdateRequired: ClientStatus()
    data object Unknown: ClientStatus()
}

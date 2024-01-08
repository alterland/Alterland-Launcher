package ru.alterland.launchercore.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Player
import ru.alterland.launchercore.domain.model.ServerProfile

interface ClientRepository {

    val serverProfiles: StateFlow<List<ServerProfile>>

    val clientProfiles: StateFlow<List<ClientProfile>>

    fun play(player: Player, serverProfile: ServerProfile)
}

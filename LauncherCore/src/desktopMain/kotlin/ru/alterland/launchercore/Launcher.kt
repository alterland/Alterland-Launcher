package ru.alterland.launchercore

import ru.alterland.launchercore.domain.repository.ClientRepository
import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Player
import ru.alterland.launchercore.domain.model.ServerProfile

class Launcher {

    private val clientRepository = KoinContext.koin.get<ClientRepository>()

    val servers: StateFlow<List<ServerProfile>> = clientRepository.serverProfiles
    val clients: StateFlow<List<ClientProfile>> = clientRepository.clientProfiles

    @Throws(Exception::class)
    fun play(player: Player, profile: ServerProfile)  {
        clientRepository.play(player, profile)
    }

}

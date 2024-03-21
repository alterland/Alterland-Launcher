package ru.alterland.launchercore

import kotlinx.coroutines.flow.StateFlow
import org.apache.logging.log4j.kotlin.Logging
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.ServerProfile
import ru.alterland.launchercore.domain.repository.ClientRepository

class Launcher {

    private val clientRepository = KoinContext.koin.get<ClientRepository>()

    val servers: StateFlow<List<ServerProfile>> = clientRepository.serverProfiles
    val clients: StateFlow<List<ClientProfile>> = clientRepository.clientProfiles
    val isOffline: StateFlow<Boolean> = clientRepository.isOffline

    @Throws(Exception::class)
    fun play(options: Options)  {
        clientRepository.play(options)
    }

    companion object : Logging

}

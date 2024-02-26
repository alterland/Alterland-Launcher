package ru.alterland.launchercore

import ru.alterland.launchercore.domain.repository.ClientRepository
import kotlinx.coroutines.flow.StateFlow
import org.apache.logging.log4j.kotlin.Logging
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.ServerProfile

class Launcher {

    private val clientRepository = KoinContext.koin.get<ClientRepository>()

    val servers: StateFlow<List<ServerProfile>> = clientRepository.serverProfiles
    val clients: StateFlow<List<ClientProfile>> = clientRepository.clientProfiles

    @Throws(Exception::class)
    fun play(options: Options)  {
        clientRepository.play(options)
    }

    companion object : Logging

}

package ru.alterland.launchercore

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.ServerProfile
import ru.alterland.launchercore.domain.repository.ClientRepository

class Launcher {

    private val clientRepository = KoinContext.koin.get<ClientRepository>()

    val servers: StateFlow<List<ServerProfile>> = clientRepository.serverProfiles
    val clients: StateFlow<List<ClientProfile>> = clientRepository.clientProfiles
    val isOffline: StateFlow<Boolean> = clientRepository.isOffline

    suspend fun fetchClientProfile(id: String): ClientProfile? = clientRepository.fetchClientProfile(id)

    @Throws(Exception::class)
    fun play(options: Options)  {
        clientRepository.play(options)
    }

    fun toggleDownload(clientProfile: ClientProfile) {
        clientRepository.toggleDownload(clientProfile)
    }
}

package ru.alterland.launchercore

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.repository.ClientRepository

class Launcher {

    private val clientRepository = KoinContext.koin.get<ClientRepository>()

    val clientProfiles: StateFlow<List<ClientProfile>> = clientRepository.clientProfiles

    suspend fun getClientProfile(id: String, force: Boolean = false): ClientProfile? =
        clientRepository.fetchClientProfile(id, force)

    fun play(options: Options) {
        clientRepository.play(options)
    }

    fun toggleDownload(clientProfile: ClientProfile) {
        clientRepository.toggleDownload(clientProfile)
    }
}

package ru.alterland.launchercore.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.ServerProfile

interface ClientRepository {

    val serverProfiles: StateFlow<List<ServerProfile>>

    val clientProfiles: StateFlow<List<ClientProfile>>

    val isOffline: StateFlow<Boolean>

    suspend fun fetchClientProfile(clientProfileId: String): ClientProfile?
    fun play(options: Options)
    fun toggleDownload(clientProfile: ClientProfile)
}

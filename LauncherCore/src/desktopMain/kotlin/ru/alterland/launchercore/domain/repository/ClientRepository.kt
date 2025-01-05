package ru.alterland.launchercore.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options

interface ClientRepository {

    val clientProfiles: StateFlow<List<ClientProfile>>

    suspend fun fetchClientProfile(id: String, force: Boolean): ClientProfile?
    fun play(options: Options)
    fun toggleDownload(clientProfile: ClientProfile)
}

package ru.alterland.launchercore.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.ServerProfile

interface ClientRepository {

    val serverProfiles: StateFlow<List<ServerProfile>>

    val clientProfiles: StateFlow<List<ClientProfile>>

    fun play(options: Options)
}

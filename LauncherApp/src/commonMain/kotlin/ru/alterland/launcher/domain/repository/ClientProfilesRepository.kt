package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.ClientProfileObject

interface ClientProfilesRepository {
    val clientProfiles: StateFlow<List<ClientProfile>>

    suspend fun updateClientProfile(id: String)

    suspend fun getClientProfileObjects(): List<ClientProfileObject>
}

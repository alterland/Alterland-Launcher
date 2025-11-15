package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.model.ServerProfile

interface ServerProfilesRepository {
    val serverProfiles: StateFlow<List<ServerProfile>>
    suspend fun addServerProfile(serverProfile: ServerProfile): ServerProfile
    suspend fun getServerProfiles()
    suspend fun getServerProfile(id: String): ServerProfile?
    suspend fun editServerProfile(serverProfile: ServerProfile): ServerProfile
}

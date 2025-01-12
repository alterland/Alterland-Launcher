package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.ServerProfile

interface ServerProfilesRepository {
    suspend fun addServerProfile(serverProfile: ServerProfile): ServerProfile
    suspend fun getServerProfiles(force: Boolean = false): List<ServerProfile>
    suspend fun getServerProfile(id: String, force: Boolean = false): ServerProfile?
    suspend fun editServerProfile(serverProfile: ServerProfile): ServerProfile
}

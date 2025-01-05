package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.mapper.toRequest
import ru.alterland.launcher.data.source.ServerProfilesApi
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.domain.repository.ServerProfilesRepository

class ServerProfilesRepositoryImpl(
    private val serverProfilesApi: ServerProfilesApi,
    private val dispatcherDefault: CoroutineDispatcher
): ServerProfilesRepository {

    private var cachedServerProfiles: List<ServerProfile> = listOf()

    override suspend fun addServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.addServerProfile(serverProfile.toRequest()).toDomain()
    }

    override suspend fun getServerProfiles(force: Boolean) = if (force || cachedServerProfiles.isEmpty()) {
        fetchServerProfiles()
    } else {
        cachedServerProfiles
    }

    override suspend fun getServerProfile(id: String, force: Boolean) = if (force) {
        fetchServerProfile(id)
    } else {
        cachedServerProfiles.firstOrNull { it.id == id }
    }

    override suspend fun editServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.editServerProfile(
            serverProfileId = serverProfile.id,
            serverProfile = serverProfile.toRequest()
        ).toDomain()
    }

    private suspend fun fetchServerProfiles() = withContext(dispatcherDefault) {
        serverProfilesApi.getServerProfiles().map { it.toDomain() }
    }.also {
        cachedServerProfiles = it
    }

    private suspend fun fetchServerProfile(id: String) = withContext(dispatcherDefault) {
        serverProfilesApi.getServerProfile(id).toDomain()
    }.also { updatedProfile ->
        val cachedServerProfilesMutable = cachedServerProfiles.toMutableList()
        val index = cachedServerProfilesMutable.indexOfFirst { it.id == id }
        if (index != -1) {
            cachedServerProfilesMutable[index] = updatedProfile
        } else {
            cachedServerProfilesMutable.add(updatedProfile)
        }
        cachedServerProfiles = cachedServerProfilesMutable
    }
}

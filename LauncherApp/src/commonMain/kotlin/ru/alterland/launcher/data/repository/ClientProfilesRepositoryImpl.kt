package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.alterland.launcher.BuildConfig
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.network.ClientProfilesApi
import ru.alterland.launcher.data.source.network.model.response.ClientProfileResponse
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.ClientProfileObject
import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.util.extentions.readJson
import ru.alterland.launcher.util.extentions.saveJson
import ru.alterland.launcher.util.extentions.v

@OptIn(ExperimentalSerializationApi::class)
class ClientProfilesRepositoryImpl(
    private val fileSystem: FileSystem,
    private val clientProfilesApi: ClientProfilesApi,
    private val dispatcherDefault: CoroutineDispatcher,
    private val dispatcherIo: CoroutineDispatcher,
    private val platformConfiguration: PlatformConfiguration,
    private val json: Json
): ClientProfilesRepository {

    private val clientProfilesDir = platformConfiguration.defaultDir v BuildConfig.CLIENT_PROFILES_FOLDER
    private val clientProfilesPath = Path(clientProfilesDir)

    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
    override val clientProfiles = _clientProfiles.asStateFlow()

    init {
        initClientProfilesPath()
    }

    override suspend fun updateClientProfile(id: String) = withContext(dispatcherDefault) {
        val clientProfilePath = Path(clientProfilesDir v "$id.json")
        val clientProfile = try {
            clientProfilesApi.getClientProfile(id).also { clientProfilePath.saveJson(value = it) }
        } catch (_: Exception) {
            clientProfilePath.readJson<ClientProfileResponse>()
        }.toDomain(json)
        updateClientProfile(clientProfile)
    }

    override suspend fun setClientStatus(clientProfile: ClientProfile, newStatus: ClientStatus) {
        clientProfiles.value.firstOrNull { it.id == clientProfile.id }?.let { profile ->
            updateClientProfile(profile.copy(status = newStatus))
        }
    }

    override suspend fun getClientProfileObjects(): List<ClientProfileObject> = withContext(dispatcherDefault) {
        clientProfilesApi.getClientProfileObjects().map { it.toDomain() }
    }

    private suspend fun updateClientProfile(value: ClientProfile) {
        val clientProfilesMutable = clientProfiles.value.toMutableList()
        val index = clientProfilesMutable.indexOfFirst { it.id == value.id }
        if (index != -1) {
            clientProfilesMutable[index] = value
        } else {
            clientProfilesMutable.add(value)
        }
        _clientProfiles.emit(clientProfilesMutable)
    }

    private fun initClientProfilesPath() = with(fileSystem) {
        if (!exists(clientProfilesPath)) {
            createDirectories(clientProfilesPath)
        }
    }

    private suspend inline fun <reified T> Path.saveJson(value: T) = withContext(dispatcherIo) {
        this@saveJson.saveJson(fileSystem = fileSystem, json = json, value = value)
    }

    private suspend inline fun <reified T> Path.readJson(): T = withContext(dispatcherIo) {
        this@readJson.readJson(fileSystem = fileSystem, json = json)
    }
}

package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import ru.alterland.launcher.BuildConfig
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.mapper.toRequest
import ru.alterland.launcher.data.source.network.ServerProfilesApi
import ru.alterland.launcher.data.source.network.model.response.ServerProfileResponse
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.util.extentions.readJson
import ru.alterland.launcher.util.extentions.saveJson
import ru.alterland.launcher.util.extentions.v

class ServerProfilesRepositoryImpl(
    private val fileSystem: FileSystem,
    private val serverProfilesApi: ServerProfilesApi,
    private val dispatcherDefault: CoroutineDispatcher,
    private val dispatcherIo: CoroutineDispatcher,
    private val platformConfiguration: PlatformConfiguration,
    private val json: Json
): ServerProfilesRepository {

    private val serverProfilesDir = platformConfiguration.defaultDir v BuildConfig.SERVER_PROFILES_FOLDER
    private val serverProfilesPath = Path(serverProfilesDir)

    private val _serverProfiles: MutableStateFlow<List<ServerProfile>> = MutableStateFlow(listOf())
    override val serverProfiles: StateFlow<List<ServerProfile>> = _serverProfiles.asStateFlow()

    init {
        initServerProfilesPath()
    }

    override suspend fun addServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.addServerProfile(serverProfile.toRequest()).toDomain()
    }

    override suspend fun getServerProfiles() {
        withContext(dispatcherDefault) {
            runCatching {
                val profiles = serverProfilesApi.getServerProfiles()
                _serverProfiles.emit(profiles.map { it.toDomain() })
                overwriteProfiles(profiles)
            }.onFailure { e ->
                val profiles = fileSystem.list(serverProfilesPath).map { it.readJson<ServerProfileResponse>() }
                _serverProfiles.emit(profiles.map { it.toDomain() })
                throw e
            }
        }
    }

    override suspend fun getServerProfile(id: String): ServerProfile? = serverProfiles.value.firstOrNull { it.id == id }

    override suspend fun editServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.editServerProfile(
            serverProfileId = serverProfile.id,
            serverProfile = serverProfile.toRequest()
        ).toDomain()
    }

    private suspend fun overwriteProfiles(newProfiles: List<ServerProfileResponse>) = withContext(dispatcherIo) {
        with(fileSystem) {
            list(serverProfilesPath).forEach { filePath ->
                metadataOrNull(filePath)?.isRegularFile?.apply {
                    delete(filePath)
                }
            }
            newProfiles.forEach { profile ->
                profile.id?.let { id ->
                    Path(serverProfilesDir v "$id.json").saveJson(profile)
                }
            }
        }
    }

    private fun initServerProfilesPath() = with(fileSystem) {
        if (!exists(serverProfilesPath)) {
            createDirectories(serverProfilesPath)
        }
    }

    private suspend inline fun <reified T> Path.saveJson(value: T) = withContext(dispatcherIo) {
        this@saveJson.saveJson(fileSystem = fileSystem, json = json, value = value)
    }

    private suspend inline fun <reified T> Path.readJson(): T = withContext(dispatcherIo) {
        this@readJson.readJson(fileSystem = fileSystem, json = json)
    }
}

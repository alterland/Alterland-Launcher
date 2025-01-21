package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
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

    private val serverProfilesDir = platformConfiguration.rootDir v BuildConfig.SERVER_PROFILES_FOLDER
    private val serverProfilesPath = Path(serverProfilesDir)

    private var cachedServerProfiles: List<ServerProfile> = listOf()

    init {
        initServerProfilesPath()
    }

    override suspend fun addServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.addServerProfile(serverProfile.toRequest()).toDomain()
    }

    override suspend fun getServerProfiles(force: Boolean) = if (force || cachedServerProfiles.isEmpty()) {
        try {
            serverProfilesApi.getServerProfiles().also {
                with(fileSystem) {
                    list(serverProfilesPath).forEach {
                        metadataOrNull(it)?.isRegularFile?.apply {
                            delete(it)
                        }
                    }
                    it.forEach {
                        it.id?.let { id ->
                            Path(serverProfilesDir v "$id.json").saveJson(it)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            val profiles = fileSystem.list(serverProfilesPath).map { it.readJson<ServerProfileResponse>() }
            if (profiles.isEmpty()) throw e else profiles
        }.map { it.toDomain() }
    } else {
        cachedServerProfiles
    }

    override suspend fun getServerProfile(id: String) = cachedServerProfiles.firstOrNull { it.id == id }

    override suspend fun editServerProfile(serverProfile: ServerProfile): ServerProfile = withContext(dispatcherDefault) {
        serverProfilesApi.editServerProfile(
            serverProfileId = serverProfile.id,
            serverProfile = serverProfile.toRequest()
        ).toDomain()
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

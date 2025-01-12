package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import ru.alterland.launcher.BuildConfig.CLIENT_PROFILES_FOLDER
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.network.ClientProfilesApi
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.ClientProfileObject
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import kotlin.io.path.Path

class ClientProfilesRepositoryImpl(
    private val clientProfilesApi: ClientProfilesApi,
    private val dispatcherDefault: CoroutineDispatcher,
    private val platformConfiguration: PlatformConfiguration
): ClientProfilesRepository {

    private val path = Path(platformConfiguration.rootDir)
    private val clientProfilesPath = path.resolve(CLIENT_PROFILES_FOLDER)

    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
    override val clientProfiles = _clientProfiles.asStateFlow()

    override suspend fun updateClientProfile(id: String) {
        withContext(dispatcherDefault) {
            val clientProfile = clientProfilesApi.getClientProfile(id)
        }
    }

    override suspend fun getClientProfileObjects(): List<ClientProfileObject> = withContext(dispatcherDefault) {
        clientProfilesApi.getClientProfileObjects().map { it.toDomain() }
    }
}

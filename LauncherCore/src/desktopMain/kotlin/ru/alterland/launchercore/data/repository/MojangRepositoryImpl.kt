package ru.alterland.launchercore.data.repository

import ru.alterland.launchercore.data.source.network.MojangApi
import ru.alterland.launchercore.data.source.network.model.response.ClientProfileResponse
import ru.alterland.launchercore.data.source.network.model.response.VersionManifestResponse
import ru.alterland.launchercore.domain.repository.MojangRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MojangRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val mojangApi: MojangApi
): MojangRepository {

    override suspend fun getClientProfile(id: String?, snapshot: Boolean): ClientProfileResponse =
        withContext(dispatcherIo) {
            val manifest = when {
                id != null -> getManifest(id)
                else -> getLatestManifest(snapshot)
            } ?: throw Exception("Client Profile not found")
            mojangApi.getClientProfile(manifest.url)
        }

    private suspend fun getManifest(id: String): VersionManifestResponse.VersionManifest? =
        mojangApi.getManifests().versions.firstOrNull { it.id == id }

    private suspend fun getLatestManifest(snapshot: Boolean = false): VersionManifestResponse.VersionManifest? {
        val manifests = mojangApi.getManifests()
        val latestId = if (snapshot) manifests.latest.snapshot else manifests.latest.release
        return manifests.versions.firstOrNull { it.id == latestId }
    }
}

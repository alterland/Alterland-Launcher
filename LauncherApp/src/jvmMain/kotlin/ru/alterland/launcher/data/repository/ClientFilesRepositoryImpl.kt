package ru.alterland.launcher.data.repository

import io.ktor.client.call.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.network.DownloadApi
import ru.alterland.launcher.data.source.network.model.response.AssetsExternalIndexesTypeResponse
import ru.alterland.launcher.data.source.network.model.response.ClientProfileResponse
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.domain.model.clientprofile.Feature
import ru.alterland.launcher.domain.model.clientprofile.Player
import ru.alterland.launcher.domain.model.clientprofile.externalindex.ExternalIndexType
import ru.alterland.launcher.domain.repository.ClientFilesRepository
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.domain.repository.LaunchRepository
import ru.alterland.launcher.util.ClientProfileUtils.testRules
import ru.alterland.launcher.util.extentions.checkSum
import ru.alterland.launcher.util.extentions.readJson
import ru.alterland.launcher.util.extentions.v

class ClientFilesRepositoryImpl(
    private val clientProfilesRepository: ClientProfilesRepository,
    private val fileSystem: FileSystem,
    private val platformConfiguration: PlatformConfiguration,
    private val downloadApi: DownloadApi,
    private val applicationIoScope: CoroutineScope,
    private val json: Json,
    private val launchRepository: LaunchRepository
): ClientFilesRepository {

    override fun updateAndLaunch(
        clientProfile: ClientProfile,
        player: Player,
        features: Map<Feature, Boolean>
    ) {
        applicationIoScope.launch {
            clientProfilesRepository.setClientStatus(clientProfile, ClientStatus.Verification)

            val externals = getExternalIndexes(clientProfile.externals)
            val downloads = getDownloads(externals).plus(getDownloads(clientProfile.downloads))

            var received = 0L
            val total = downloads.sumOf { it.size }

            val errorIndexes = mutableListOf<ClientProfile.DownloadIndex>()

            batchDownload(
                downloads = downloads,
                onError = { e, downloadIndex ->
                    errorIndexes.add(downloadIndex)
                }
            ) { i ->
                received += i
                clientProfilesRepository.setClientStatus(
                    clientProfile,
                    ClientStatus.Updating(received = received, total = total)
                )
            }
            if (errorIndexes.isNotEmpty()) {
                clientProfilesRepository.setClientStatus(
                    clientProfile,
                    ClientStatus.UpdateError(errorIndexes.size)
                )
            } else {
                launchRepository.launch(clientProfile, player, features)
            }
        }
    }

    private suspend fun getExternalIndexes(indexes: List<ClientProfile.ExternalIndex>) =
        indexes
            .filter { testRules(it.rules) }
            .flatMap { index ->
                val indexPath = Path(platformConfiguration.defaultDir v index.indexPath)
                val checkSum = if (fileSystem.exists(indexPath)) indexPath.checkSum(fileSystem) else null
                if (checkSum == null || index.checkSum != checkSum) {
                    fileSystem.delete(indexPath, mustExist = false)
                    downloadFile(index.url, indexPath)
                }

                when (index.type) {
                    ExternalIndexType.ASSETS ->
                        indexPath.readJson<AssetsExternalIndexesTypeResponse>().toDomain(index.externalsPath)
                    ExternalIndexType.DEFAULT ->
                        indexPath.readJson<List<ClientProfileResponse.DownloadIndex>>().mapNotNull {
                            it.toDomain(index.externalsPath)
                        }
                }
            }

    private fun getDownloads(indexes: List<ClientProfile.DownloadIndex>) =
        indexes
            .filter { testRules(it.rules) }
            .filter { index ->
                val indexPath = Path(platformConfiguration.defaultDir v index.path)
                val checkSum = if (fileSystem.exists(indexPath)) indexPath.checkSum(fileSystem) else null
                (checkSum == null || index.checkSum != checkSum && !index.allowChanges).also {
                    if (it) println("Хеш файла $indexPath отличается.\nОжидается: ${index.checkSum}\nФактически: $checkSum")
                }
            }

    private suspend fun downloadFile(
        downloadUrl: String,
        savePath: Path,
        onBytesReceived: suspend (Long) -> Unit = {}
    ) {
        downloadApi.download(downloadUrl).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            savePath.parent?.let { parent ->
                if (!fileSystem.exists(parent)) {
                    fileSystem.createDirectories(parent)
                }
            }
            fileSystem.sink(savePath).buffered().use { sink ->
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                    while (!packet.exhausted()) {
                        onBytesReceived(packet.remaining)
                        sink.writePacket(packet)
                    }
                }
            }
        }
    }

    private suspend fun batchDownload(
        downloads: List<ClientProfile.DownloadIndex>,
        maxConcurrentDownloads: Int = 3,
        onError: (Exception, ClientProfile.DownloadIndex) -> Unit = { _, _ -> },
        onBytesReceived: suspend (Long) -> Unit = {}
    ) = coroutineScope {
        val semaphore = Semaphore(maxConcurrentDownloads)
        downloads.map {
            async {
                semaphore.acquire()
                try {
                    val path = Path(platformConfiguration.defaultDir v it.path)
                    fileSystem.delete(path, mustExist = false)
                    downloadFile(downloadUrl = it.url, savePath = path, onBytesReceived)
                } catch (e: Exception) {
                    onError(e, it)
                } finally {
                    semaphore.release()
                }
            }
        }.awaitAll()
    }

    private inline fun <reified T> Path.readJson(): T =
        this@readJson.readJson(fileSystem = fileSystem, json = json)
}

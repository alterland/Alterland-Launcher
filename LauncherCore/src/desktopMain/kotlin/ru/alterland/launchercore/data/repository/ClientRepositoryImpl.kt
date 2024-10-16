package ru.alterland.launchercore.data.repository

import AlterlandLauncher.LauncherCore.BuildConfig
import AlterlandLauncher.LauncherCore.BuildConfig.CLIENT_PROFILES_FOLDER
import AlterlandLauncher.LauncherCore.BuildConfig.SERVER_PROFILES_FOLDER
import io.ktor.client.call.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.io.readByteArray
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import ru.alterland.launchercore.data.mapper.toDomain
import ru.alterland.launchercore.data.source.local.model.AssetsExternalIndexesTypeRaw
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw
import ru.alterland.launchercore.data.source.local.model.ServerProfileRaw
import ru.alterland.launchercore.data.source.network.ClientApi
import ru.alterland.launchercore.domain.model.*
import ru.alterland.launchercore.domain.model.externalindex.ExternalIndexType
import ru.alterland.launchercore.domain.repository.ClientRepository
import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.domain.repository.ServerRepository
import ru.alterland.launchercore.dto.LaunchOptions
import ru.alterland.launchercore.util.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*


@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
class ClientRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val applicationScope: CoroutineScope,
    private val applicationIoScope: CoroutineScope,
    private val serverRepository: ServerRepository,
    private val launchRepository: LaunchRepository,
    private val clientApi: ClientApi,
    private val jsonReader: Json
): ClientRepository {

    private val jsonWriter = Json {
        explicitNulls = false
    }

    private val workPath = Path("$USER_HOME/${BuildConfig.WORK_FOLDER}")
    private val serverProfilesPath = workPath.resolve(SERVER_PROFILES_FOLDER)
    private val clientProfilesPath = workPath.resolve(CLIENT_PROFILES_FOLDER)

    private val _serverProfiles = MutableStateFlow<List<ServerProfile>>(listOf())
    override val serverProfiles = _serverProfiles.asStateFlow()

    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
    override val clientProfiles = _clientProfiles.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    override val isOffline = _isOffline.asStateFlow()

    init {
        initProfiles()
    }

    override suspend fun fetchClientProfile(clientProfileId: String): ClientProfile? = withContext(dispatcherIo) {
        try {
            val rawProfile = clientApi.getClientProfile(clientProfileId)
            rawProfile.id?.let { jsonWriter.encodeToStream(rawProfile, clientProfilesPath.resolve(it).outputStream()) }
            val profile = rawProfile.toDomain(jsonReader)
            val updateClientProfiles = _clientProfiles.value.toMutableList().apply { add(profile) }
            _clientProfiles.emit(updateClientProfiles)
            _isOffline.emit(false)
            profile
        } catch (e: Exception) {
            _isOffline.emit(true)
            clientProfiles.value.firstOrNull { it.id == clientProfileId }
        }
    }

    override fun play(options: Options) {
        val player = options.player
        val features = options.features.mapKeys { it.key.value }

        options.clientProfile.updateClientStatus(ClientStatus.Verification)

        applicationScope.launch {
            val clientProfile = fetchClientProfile(options.clientProfile.id) ?: options.clientProfile
            val isUpdatedSuccessFully = clientProfile.updateClient()
            if (isUpdatedSuccessFully) {
                clientProfile.cleanStrictPaths()
                clientProfile.launch(player, features)
            }
        }
    }

    override fun toggleDownload(clientProfile: ClientProfile) {
        if (clientProfile.status is ClientStatus.Updating) {

        }
    }

    private fun ClientProfile.cleanStrictPaths() {
        val startTime = System.currentTimeMillis()
        strict.forEach {
            workPath.resolve(it).walk().forEach { path ->
                val index = downloads.firstOrNull { index -> path.endsWith(index.path) }
                if (index == null) path.deleteIfExists()
            }
        }
        val endTime = System.currentTimeMillis()
        println("elapsed:${endTime-startTime} ms")
    }

    private suspend fun ClientProfile.updateClient(): Boolean {
        val externals = getExternalIndexes(externals)
        val downloads = getDownloads(externals).plus(getDownloads(downloads))

        var received = 0L
        val total = downloads.sumOf { it.size }

        val errorIndexes = mutableListOf<ClientProfile.DownloadIndex>()

        download(
            downloads = downloads,
            onError = { e, downloadIndex ->
                errorIndexes.add(downloadIndex)
            }
        ) { i ->
            received += i
            updateClientStatus(ClientStatus.Updating(received = received, total = total))
        }
        return if (errorIndexes.isNotEmpty()) {
            updateClientStatus(ClientStatus.UpdateError(errorIndexes.size))
            false
        } else {
            true
        }
    }

    private fun ClientProfile.launch(
        player: Player,
        features: Map<String, Boolean>
    ) {
        updateClientStatus(ClientStatus.Launching)

        val gameArguments = gameArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
        val jvmArguments = jvmArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
        val classPath = downloads.filter { it.classPath && testRules(it.rules) }.map { workPath.resolve(it.path).toString() }

        val launchOptions = LaunchOptions(
            id = id,
            gameArguments = gameArguments,
            jvmArguments = jvmArguments,
            workPath = workPath,
            accessToken = player.accessToken,
            uuid = player.id,
            nickname = player.nickname,
            classPath = classPath.joinToString(File.pathSeparator),
            mainClass = mainClass
        )
        launchRepository.launch(launchOptions)
        updateClientStatus(ClientStatus.Launched)
    }

    private suspend fun getExternalIndexes(indexes: List<ClientProfile.ExternalIndex>) = withContext(dispatcherIo) {
        indexes
            .filter { testRules(it.rules) }
            .flatMap { index ->
                val indexPath = workPath.resolve(index.indexPath)
                val checkSum = if (indexPath.exists()) indexPath.getCheckSumFromFile() else null
                if (checkSum == null || index.checkSum != checkSum) {
                    indexPath.deleteFileAndCreateEmpty()
                    downloadFile(index.url, indexPath)
                }
                val basePath = workPath.resolve(index.externalsPath)
                when(index.type) {
                    ExternalIndexType.ASSETS ->
                        jsonReader.decodeFromStream<AssetsExternalIndexesTypeRaw>(indexPath.inputStream())
                            .toDomain(basePath)
                    ExternalIndexType.DEFAULT ->
                        jsonReader.decodeFromStream<List<ClientProfileRaw.DownloadIndex>>(indexPath.inputStream())
                            .mapNotNull { it.toDomain(basePath) }
                }
        }
    }

    private suspend fun getDownloads(indexes: List<ClientProfile.DownloadIndex>) = withContext(dispatcherIo) {
        indexes
            .filter { testRules(it.rules) }
            .filter { index ->
                val indexPath = workPath.resolve(index.path)
                val checkSum = if (indexPath.exists()) indexPath.getCheckSumFromFile() else null
                checkSum == null || index.checkSum != checkSum && !index.allowChanges
            }
    }

    private fun initProfiles() = applicationIoScope.launch {
        serverProfilesPath.createDirectories()
        clientProfilesPath.createDirectories()
        try {
            val profiles = clientApi.getServerProfiles()
            profiles.forEach { profile ->
                profile.id?.let { jsonWriter.encodeToStream(profile, serverProfilesPath.resolve(it).outputStream()) }
            }
            _isOffline.emit(false)
        } catch (e: Exception) {
            println(e)
            _isOffline.emit(true)
        } finally {
            initServerProfiles()
            initClientProfiles()
        }
    }

    private fun initServerProfiles() {
        println("Поиск профилей серверов в папке $serverProfilesPath")
        val serverProfiles = mutableListOf<ServerProfile>()
        serverProfilesPath.walk().forEach { path ->
            if (path.isRegularFile()) {
                val serverProfileRaw = jsonReader.decodeFromStream<ServerProfileRaw>(path.inputStream())
                val serverProfile = serverProfileRaw.toDomain()
                serverProfiles.add(serverProfile)
            }
        }
        serverProfiles.sortBy { it.sortIndex }
        _serverProfiles.tryEmit(serverProfiles)
        pingServers()
    }

    private fun initClientProfiles() {
        println("Поиск профилей клиентов в папке $clientProfilesPath")
        val clientProfiles = mutableListOf<ClientProfile>()
        clientProfilesPath.walk().forEach { path ->
            if (path.isRegularFile()) {
                val clientProfileRaw = jsonReader.decodeFromStream<ClientProfileRaw>(path.inputStream())
                val clientProfile = clientProfileRaw.toDomain(jsonReader)
                clientProfiles.add(clientProfile)
            }
        }
        _clientProfiles.tryEmit(clientProfiles)
    }

    private suspend fun download(
        downloads: List<ClientProfile.DownloadIndex>,
        maxConcurrentDownloads: Int = 3,
        onError: (Exception, ClientProfile.DownloadIndex) -> Unit = { _, _ -> },
        onBytesReceived: (Int) -> Unit = {}
    ) = coroutineScope {
        val semaphore = Semaphore(maxConcurrentDownloads)
        downloads.map {
            async {
                semaphore.acquire()
                try {
                    val path = workPath.resolve(it.path)
                    path.deleteFileAndCreateEmpty()
                    downloadFile(downloadUrl = it.url, savePath = path, onBytesReceived)
                } catch (e: Exception) {
                    onError(e, it)
                } finally {
                    semaphore.release()
                }
            }
        }.awaitAll()
    }

    private suspend fun downloadFile(
        downloadUrl: String,
        savePath: Path,
        onBytesReceived: (Int) -> Unit = {}
    ) = withContext(dispatcherIo) {
        clientApi.downloadFile(downloadUrl).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.exhausted()) {
                    val bytes = packet.readByteArray()
                    savePath.appendBytes(bytes)
                    onBytesReceived(bytes.size)
                }
            }
        }
    }

    private fun Path.removeEmptyParentFolders() {
        val stream = Files.newDirectoryStream(parent)
        if (!stream.iterator().hasNext()) {
            parent.deleteExisting()
            stream.close()
            parent.removeEmptyParentFolders()
        }
        stream.close()
    }

    private fun testRules(
        rules: List<ClientProfile.Rule>,
        enabledFeatures: Map<String, Boolean>? = null
    ): Boolean {
        var testPass = true
        for (rule in rules) {
            if (rule.action == null) continue

            if (rule.os != null) {
                val osNameMatch = if (rule.os.name != null && USER_OS.name != null) {
                    USER_OS.name == rule.os.name
                } else true

                val osArchMatch = if (rule.os.arch != null && USER_OS.arch != null) {
                    USER_OS.arch == rule.os.arch
                } else true

                val osVersionMatch = if (rule.os.version != null && USER_OS.version != null) {
                    Regex(rule.os.version).containsMatchIn(USER_OS.version)
                } else true

                testPass = when(rule.action) {
                    ActionRule.ALLOW -> osNameMatch && osArchMatch && osVersionMatch
                    ActionRule.DISALLOW -> !osNameMatch || !osArchMatch || !osVersionMatch
                }
            }

            if (enabledFeatures != null) {
                rule.features.forEach { feature ->
                    testPass = testPass && feature.value == (enabledFeatures[feature.key] ?: false)
                }
            }
        }
        return testPass
    }

    private fun pingServers() {
        applicationIoScope.launch {
            val serverPings = serverProfiles.value
                .filter { it.address != null }
                .map {
                    async {
                        val pong = serverRepository.ping(it.address!!.ip, it.address.port)
                        val updateClients = serverProfiles.value.map { updateClient ->
                            if (updateClient.id == it.id) {
                                updateClient.copy(pong = pong)
                            } else updateClient
                        }
                        _serverProfiles.emit(updateClients)
                    }
                }
            serverPings.awaitAll()
            delay(PING_DELAY)
            pingServers()
        }
    }

    private fun ClientProfile.updateClientStatus(newStatus: ClientStatus) {
        val clientsMutable = _clientProfiles.value.toMutableList()
        val client = clientsMutable.firstOrNull { it.id == id } ?: return
        val index = clientsMutable.indexOf(client)
        clientsMutable[index] = client.copy(status = newStatus)
        _clientProfiles.tryEmit(clientsMutable)
    }

    companion object {
        private val USER_OS = OS(name = OS_NAME, arch = OS_ARCH, version = OS_VERSION)
        private const val PING_DELAY = 5000L
    }

}

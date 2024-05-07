package ru.alterland.launchercore.data.repository

import AlterlandLauncher.LauncherCore.BuildConfig
import AlterlandLauncher.LauncherCore.BuildConfig.CLIENT_PROFILES_FOLDER
import AlterlandLauncher.LauncherCore.BuildConfig.SERVER_PROFILES_FOLDER
import io.ktor.client.call.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import ru.alterland.launchercore.data.mapper.getLibrary
import ru.alterland.launchercore.data.mapper.toDomain
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw
import ru.alterland.launchercore.data.source.local.model.ServerProfileRaw
import ru.alterland.launchercore.data.source.network.ClientApi
import ru.alterland.launchercore.data.source.network.model.response.AssetsIndexResponse
import ru.alterland.launchercore.data.source.network.model.response.ExternalIndexResponse
import ru.alterland.launchercore.domain.model.*
import ru.alterland.launchercore.domain.repository.ClientRepository
import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.domain.repository.ServerRepository
import ru.alterland.launchercore.dto.LaunchOptions
import ru.alterland.launchercore.util.HashUtils.getCheckSumFromFile
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission
import java.security.MessageDigest
import kotlin.io.path.*


@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
class ClientRepositoryImpl(
    private val applicationIoScope: CoroutineScope,
    private val launchDispatcher: CoroutineDispatcher,
    private val serverRepository: ServerRepository,
    private val launchRepository: LaunchRepository,
    private val clientApi: ClientApi,
    private val jsonReader: Json
): ClientRepository {

    private val jsonWriter = Json {
        prettyPrint = true
    }

    private val workPath = Path("$USER_HOME/${BuildConfig.WORK_FOLDER}")
    private val assetsPath = workPath.resolve(ASSETS_FOLDER)
    private val serverProfilesPath = workPath.resolve(SERVER_PROFILES_FOLDER)
    private val clientProfilesPath = workPath.resolve(CLIENT_PROFILES_FOLDER)

    private val _serverProfiles = MutableStateFlow<List<ServerProfile>>(listOf())
    override val serverProfiles = _serverProfiles.asStateFlow()

    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
    override val clientProfiles = _clientProfiles.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    override val isOffline = _isOffline.asStateFlow()

    private val hashAlgorithm = MessageDigest.getInstance("SHA-1")

    init {
        serverProfilesPath.createDirectories()
        clientProfilesPath.createDirectories()
        getServerProfiles()
        initClientProfiles()
    }

    override fun play(options: Options) {
        val serverProfile = options.serverProfile
        val player = options.player
        val features = options.features.mapKeys { it.key.value }

        if (serverProfile.clientProfile == null) return

        getClientProfileOrStubLocal(serverProfile.clientProfile).apply {
            updateClientStatus(ClientStatus.Verification)
        }

        applicationIoScope.launch {
            updateProfileAndDownload(serverProfile.clientProfile, features, player)
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
                val clientProfile = clientProfileRaw.toDomain()
                clientProfiles.add(clientProfile)
            }
        }
        _clientProfiles.tryEmit(clientProfiles)
    }

    private suspend fun updateProfileAndDownload(
        clientProfileId: String,
        features: Map<String, Boolean>,
        player: Player
    ) {
        getClientProfile(clientProfileId)?.apply {
            val mainModulePath =  workPath.resolve(id)

            val gameArguments = gameArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
            val jvmArguments = jvmArguments.filter { testRules(it.rules, features) }.flatMap { it.value }

            val childModulesSums = getFoldersFilesChecksums(modules.filter { it != id && !it.contains("runtime") })
            val mainModuleSums = getFolderFilesChecksums(id)
            val assetsSums = getFolderFilesChecksums(ASSETS_OBJECTS_FOLDER)
            val strictSums = mainModuleSums.filter {
                val segments = it.key.split('/')
                segments.size > 1 && strict.contains(segments[1])
            }
            val allModulesSums = childModulesSums + mainModuleSums

            val externalSums = mutableMapOf<String, String?>()
            external.forEach {
                externalSums.putAll(getFolderFilesChecksums(workPath.resolve(it.path).parent.toAbsolutePath().toString()))
            }

            var runtime: Path? = null

            val assetObjects = getAssetObjects()
            val externalObjects = external.filter { testRules(it.rules, features) }.flatMap {
                if (it.path.contains("runtime")) {
                    val configPath = workPath.resolve(it.path)
                    runtime = configPath.parent.resolve("${configPath.nameWithoutExtension}/bin").apply {
                        if (IS_POSIX) {
                            Files.setPosixFilePermissions(this, BIN_POSIX_PERMISSIONS)
                        }
                    }
                }
                getExternalObjectsFromIndex(it)
            }

            deleteWrongFiles(childModulesSums, libraries)
            deleteWrongFiles(strictSums, extra)
            deleteWrongFiles(assetsSums, assetObjects, true)
            deleteWrongFiles(externalSums, externalObjects)
            val downloads = mutableListOf<ClientProfile.Library>().apply {
                addAll(libraries.getDownloads(allModulesSums))
                addAll(extra.getDownloads(allModulesSums))
                addAll(assetObjects.getDownloads(assetsSums))
                addAll(externalObjects.getDownloads(externalSums))
            }

            try {
                download(downloads)

                updateClientStatus(ClientStatus.Launching)

                val authlibPath = libraries.find { it.downloads?.artifact?.path?.contains("authlibinjector") == true }?.let {
                    workPath.resolve(it.downloads!!.artifact!!.path)
                }?.toAbsolutePath()?.toString()

                val classPath = libraries.filter { it.downloads?.artifact?.path != null }.map {
                    workPath.resolve(it.downloads!!.artifact!!.path).toAbsolutePath().toString()
                }

                val launchOptions = LaunchOptions(
                    gameArguments = gameArguments,
                    jvmArguments = jvmArguments,
                    gameDir = mainModulePath.toAbsolutePath().toString(),
                    jvmDir = runtime?.resolve("java")?.toAbsolutePath()?.toString() ?: "",
                    authLibInjectorPath = authlibPath,
                    nativesDir = mainModulePath.resolve(NATIVES_FOLDER).toAbsolutePath().toString(),
                    assetIndex = assets?.id ?: "",
                    assetsDir = assetsPath.toAbsolutePath().toString(),
                    accessToken = player.accessToken,
                    uuid = player.id,
                    versionName = id,
                    versionType = type,
                    nickname = player.nickname,
                    classPath = classPath.joinToString(File.pathSeparator),
                    mainClass = mainClass ?: "net.minecraft.client.main.Main"
                )

                withContext(launchDispatcher) {
                    launchRepository.launch(launchOptions)
                    updateClientStatus(ClientStatus.Launched)
                }
            } catch (e: Exception) {
                updateClientStatus(ClientStatus.DownloadError)
            }
        }
    }

    private suspend fun ClientProfile.getAssetObjects(): List<ClientProfile.Library> {
        assets?.let { index ->
            val indexPath = workPath.resolve("$ASSETS_INDEXES_FOLDER/${index.id}.$ASSETS_INDEXES_EXT")
            if (indexPath.exists()) {
                val localCheckSum = indexPath.getCheckSumFromFile(hashAlgorithm, 40)
                if (localCheckSum == index.checkSum) {
                    return getAssetsIndexes(indexPath)
                }
            }
            indexPath.deleteIfExists()
            indexPath.createParentDirectories()
            indexPath.createFile()
            downloadAndSaveFile(index.url, indexPath, index.totalSize)
            return getAssetObjects()
        }
        return listOf()
    }

    private fun getAssetsIndexes(indexPath: Path): List<ClientProfile.Library> {
        val assets = mutableListOf<ClientProfile.Library>()
        val config = jsonReader.decodeFromStream<AssetsIndexResponse>(indexPath.inputStream())
        config.objects?.let { objects ->
            objects.forEach { (_, obj) ->
                if (obj.hash != null && obj.hash.length > 2) {
                    val firstTwo = obj.hash.substring(0, 2)
                    val url = "${BuildConfig.MOJANG_ASSETS_HOST}/$firstTwo/${obj.hash}"
                    val download = ClientProfile.Library(
                        downloads = ClientProfile.Downloads(
                            artifact = ClientProfile.Artifact(
                                path = "$ASSETS_OBJECTS_FOLDER/$firstTwo/${obj.hash}",
                                checkSum = obj.hash,
                                size = obj.size ?: 0L,
                                url = url
                            )
                        )
                    )
                    assets.add(download)
                }
            }
        }
        return assets
    }

    private suspend fun ClientProfile.getExternalObjectsFromIndex(index: ClientProfile.ExternalIndex): List<ClientProfile.Library> {
        val indexPath = workPath.resolve(index.path)
        if (indexPath.exists()) {
            val localCheckSum = indexPath.getCheckSumFromFile(hashAlgorithm, 40)
            if (localCheckSum == index.checkSum) {
                return jsonReader.decodeFromStream<ExternalIndexResponse>(indexPath.inputStream()).objects?.map {
                    it.getLibrary()
                }?.map { lib ->
                    lib.copy(
                        downloads = lib.downloads?.copy(
                            artifact = lib.downloads.artifact?.copy(
                                path = "${indexPath.parent.fileName}/${lib.downloads.artifact.path}"
                            )
                        )
                    )
                } ?: listOf()
            }
        }
        indexPath.deleteIfExists()
        indexPath.createParentDirectories()
        indexPath.createFile()
        downloadAndSaveFile(index.url, indexPath, index.totalSize)
        return getExternalObjectsFromIndex(index)
    }

    private suspend fun getClientProfile(clientProfileName: String): ClientProfile? =
        try {
            val rawProfile = clientApi.getClientProfile(clientProfileName)
            rawProfile.id?.let {
                clientProfilesPath.resolve(it).writeObj(rawProfile)
            }
            val profile = rawProfile.toDomain()
            val updateClientProfiles = clientProfiles.value.map {
                if (it.id == clientProfileName) profile.copy(status = it.status) else it
            }
            _clientProfiles.emit(updateClientProfiles)
            _isOffline.emit(false)
            profile
        } catch (e: Exception) {
            _isOffline.emit(true)
            clientProfiles.value.firstOrNull { it.id == clientProfileName }
        }

    private fun getServerProfiles() = applicationIoScope.launch {
        try {
            val profiles = clientApi.getServerProfiles()
            profiles.forEach { rawProfile ->
                rawProfile.id?.let { id ->
                    serverProfilesPath.resolve(id).writeObj(rawProfile)
                }
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

    private fun deleteWrongFiles(locals: Map<String, String?>, remotes: List<ClientProfile.Library>, onlyHashDifferent: Boolean = false) {
        val remotesMap = mutableMapOf<String, String>()
        remotes.forEach { lib ->
            lib.downloads?.artifact?.let {
                remotesMap[it.path] = it.checkSum
            }
        }
        deleteWrongFiles(locals, remotesMap, onlyHashDifferent)
    }

    private fun deleteWrongFiles(locals: Map<String, String?>, remotes: Map<String, String>, onlyHashDifferent: Boolean = false) {
        locals.forEach { entry ->
            val remote = remotes[entry.key]
            if (remote == null && !onlyHashDifferent || remote != null && remote != entry.value) {
                val file = workPath.resolve(entry.key)
                file.deleteIfExists()
                file.removeEmptyParentFolders()
            }
        }
    }

    private fun List<ClientProfile.Library>.getDownloads(locals: Map<String, String?>): List<ClientProfile.Library> {
        val downloads = mutableListOf<ClientProfile.Library>()
        this.filter { testRules(it.rules) }.forEach { library ->
            library.downloads?.artifact?.let { artifact ->
                val checkSum = locals[artifact.path]
                if (checkSum == null || checkSum != artifact.checkSum) {
                    downloads.add(library)
                }
            }
        }
        return downloads
    }

    private suspend fun ClientProfile.download(libraries: List<ClientProfile.Library>) {
        val total = libraries.sumOf { it.downloads?.artifact?.size ?: 0 }
        libraries.forEach { library ->
            library.downloads?.artifact?.let { artifact ->
                if (artifact.url.isNotEmpty() && artifact.path.isNotEmpty()) {
                    val saveFile = workPath.resolve(artifact.path)
                    saveFile.deleteIfExists()
                    saveFile.createParentDirectories()
                    saveFile.createFile()
                    downloadAndSaveFile(artifact.url, saveFile, total)
                }
            }
        }
    }

    private suspend fun ClientProfile.downloadAndSaveFile(downloadUrl: String, saveFile: Path, total: Long) {
        clientApi.downloadFile(downloadUrl).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    saveFile.appendBytes(bytes)
                    updateClientDownloadStatus(bytes.size, total)
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

    private fun getFoldersFilesChecksums(folderPaths: List<String>): Map<String, String?> {
        val checkSums = mutableMapOf<String, String?>()
        folderPaths.forEach {
            val map = getFolderFilesChecksums(it)
            checkSums.putAll(map)
        }
        return checkSums
    }

    private fun getFolderFilesChecksums(folderPath: String): Map<String, String?> {
        val checkSums = mutableMapOf<String, String?>()
        val path = workPath.resolve(folderPath)
        if (path.exists()) {
            path.walk().forEach { subPath ->
                if (subPath.isRegularFile()) {
                    val checkSum = subPath.getCheckSumFromFile(hashAlgorithm, 40)
                    val relativePath = subPath.relativeTo(workPath).toString().replace("\\", "/")
                    checkSums[relativePath] = checkSum
                }
            }
        }
        return checkSums
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

    private inline fun <reified T> Path.writeObj(obj: T): T {
        jsonWriter.encodeToStream<T>(obj, this.outputStream())
        return jsonReader.decodeFromStream<T>(this.inputStream())
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
        val client = clientsMutable.firstOrNull { it.id == this.id } ?: return
        val index = clientsMutable.indexOf(client)
        clientsMutable[index] = client.copy(status = newStatus)
        _clientProfiles.tryEmit(clientsMutable)
    }

    private fun ClientProfile.updateClientDownloadStatus(newBytesSize: Int, total: Long) {
        val clientsMutable = clientProfiles.value.toMutableList()
        val client = clientsMutable.firstOrNull { it.id == this.id } ?: return
        val index = clientsMutable.indexOf(client)
        val newStatus = if (client.status is ClientStatus.Downloading) {
            ClientStatus.Downloading(
                received = client.status.received + newBytesSize,
                total = client.status.total
            )
        } else {
            ClientStatus.Downloading(
                received = newBytesSize.toLong(),
                total = total
            )
        }
        clientsMutable[index] = client.copy(status = newStatus)
        _clientProfiles.tryEmit(clientsMutable)
    }

    private fun getClientProfileOrStubLocal(id: String): ClientProfile {
        val clientProfilesMutable = _clientProfiles.value.toMutableList()
        var clientProfile = clientProfilesMutable.firstOrNull { it.id == id }
        if (clientProfile == null) {
            clientProfile = ClientProfileRaw(id = id).toDomain()
            clientProfilesMutable.add(clientProfile)
            _clientProfiles.tryEmit(clientProfilesMutable)
        }
        return clientProfile
    }

    companion object {
        private val USER_HOME = System.getProperty("user.home")
        private val OS_NAME = OsName.getOsType(System.getProperty("os.name"))
        private val OS_ARCH = OsArch.getOsArchType(System.getProperty("os.arch"))
        private val OS_VERSION = System.getProperty("os.version")
        private val USER_OS = OS(name = OS_NAME, arch = OS_ARCH, version = OS_VERSION)

        private val IS_POSIX = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")
        private val BIN_POSIX_PERMISSIONS = setOf(
            PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, // Owner
            PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_EXECUTE, // Group
            PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE // Others
        )

        private const val NATIVES_FOLDER = "bin"
        private const val ASSETS_FOLDER = "assets"
        private const val ASSETS_INDEXES_EXT = "json"
        private const val ASSETS_INDEXES_FOLDER = "$ASSETS_FOLDER/indexes"
        private const val ASSETS_OBJECTS_FOLDER = "$ASSETS_FOLDER/objects"

        private const val PING_DELAY = 5000L
    }

}

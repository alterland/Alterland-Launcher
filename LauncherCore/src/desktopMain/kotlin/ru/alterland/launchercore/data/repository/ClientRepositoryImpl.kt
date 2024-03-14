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
import ru.alterland.launchercore.data.mapper.toDomain
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw
import ru.alterland.launchercore.data.source.local.model.ServerProfileRaw
import ru.alterland.launchercore.data.source.network.ClientApi
import ru.alterland.launchercore.domain.model.*
import ru.alterland.launchercore.domain.repository.ClientRepository
import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.domain.repository.ServerRepository
import ru.alterland.launchercore.util.HashUtils.getCheckSumFromFile
import ru.alterland.launchercore.util.v
import java.nio.file.Path
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

    private val workPath = Path(USER_HOME v BuildConfig.WORK_FOLDER)
    private val assetsPath = workPath.resolve(ASSETS_FOLDER)
    private val assetsIndexesPath = workPath.resolve(ASSETS_INDEXES_FOLDER)
    private val assetsObjectsPath = workPath.resolve(ASSETS_OBJECTS_FOLDER)
    private val serverProfilesPath = workPath.resolve(SERVER_PROFILES_FOLDER)
    private val clientProfilesPath = workPath.resolve(CLIENT_PROFILES_FOLDER)

    private val _serverProfiles = MutableStateFlow<List<ServerProfile>>(listOf())
    override val serverProfiles = _serverProfiles.asStateFlow()

    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
    override val clientProfiles = _clientProfiles.asStateFlow()

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

        getClientProfileOrStub(serverProfile.clientProfile).apply {
            updateClientStatus(ClientStatus.Verification)
        }

        applicationIoScope.launch {
            updateProfileAndDownload(serverProfile.clientProfile, features)
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
        features: Map<String, Boolean>
    ) {
        getClientProfile(clientProfileId)?.apply {
            val gameArguments = gameArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
            val jvmArguments = jvmArguments.filter { testRules(it.rules, features) }.flatMap { it.value }

            val locals = checkLocals()
            val downloads = getDownloads(locals)

            download(downloads)

            //check and download again recursively while downloads is not empty (case: client updated while client download)

            updateClientStatus(ClientStatus.Launching)

//            val launchOptions = LaunchOptions(
//                gameArguments = gameArguments,
//                jvmArguments = jvmArguments,
//                gameDir = clientPath,
//                jvmDir = JVM_DIR,
//                authLibInjectorPath = authLibInjectorPath,
//                nativesDir = nativesPath,
//                assetIndex = assetsIndexName ?: "",
//                assetsDir = assetsPath,
//                accessToken = player.accessToken,
//                uuid = player.id,
//                versionName = baseVersion,
//                versionType = versionType,
//                nickname = player.nickname,
//                classPath = classPath.joinToString(":"),
//                mainClass = mainClass ?: ""
//            )
//
//            withContext(launchDispatcher) {
//                launchRepository.launch(launchOptions)
//                updateClientStatus(ClientStatus.Launched)
//            }
        }
    }

    private suspend fun getClientProfile(clientProfileName: String): ClientProfile? =
        try {
            val rawProfile = clientApi.getClientProfile(clientProfileName)
            rawProfile.id?.let {
                clientProfilesPath.resolve(it).writeObj(rawProfile)
            }
            val profile = rawProfile.toDomain()
            val updateClientProfiles = clientProfiles.value.map {
                if (it.id == clientProfileName) profile else it
            }
            _clientProfiles.tryEmit(updateClientProfiles)
            profile
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            println(e)
            //TODO switch to offline mode
        } finally {
            initServerProfiles()
            initClientProfiles()
        }
    }

    private fun ClientProfile.checkLocals(): Map<String, String> {
        val locals = mutableMapOf<String, String>()

        modules.forEach { module ->
            val modulePath = workPath.resolve(module)
            if (modulePath.exists()) {
                modulePath.walk().forEach { path ->
                    if (path.isRegularFile()) {
                        val checkSum = path.getCheckSumFromFile(hashAlgorithm, 40)
                        val relativePath = path.relativeTo(workPath).toString()
                        locals[relativePath] = checkSum
                    }
                }
            }
        }

        return locals
    }

    private fun ClientProfile.getDownloads(locals: Map<String, String>): List<ClientProfile.Library> {
        val downloads = mutableListOf<ClientProfile.Library>()

        libraries.filter { testRules(it.rules) }.forEach { library ->
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
                    if (saveFile.exists()) {
                        saveFile.deleteRecursively()
                    }
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
                        val pong = serverRepository.ping(it.address!!.getAddress())
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

    private fun getClientProfileOrStub(id: String): ClientProfile {
        val clientProfilesMutable = _clientProfiles.value.toMutableList()
        var clientProfile = clientProfilesMutable.firstOrNull { it.id == id }
        if (clientProfile == null) {
            clientProfile = ClientProfileRaw(
                id, null, null, null,
                null, null, null, null, null
            ).toDomain()
            clientProfilesMutable.add(clientProfile)
            _clientProfiles.tryEmit(clientProfilesMutable)
        }
        return clientProfile
    }

    companion object {
        private val USER_HOME = System.getProperty("user.home")
        private val JVM_DIR = System.getProperty("java.home") v "bin" v "java"
        private val OS_NAME = OsName.getOsType(System.getProperty("os.name"))
        private val OS_ARCH = OsArch.getOsArchType(System.getProperty("os.arch"))
        private val OS_VERSION = System.getProperty("os.version")
        private val USER_OS = OS(name = OS_NAME, arch = OS_ARCH, version = OS_VERSION)

        private const val NATIVES_FOLDER = "bin"
        private const val ASSETS_FOLDER = "assets"
        private const val ASSETS_INDEXES_FOLDER = "indexes"
        private const val ASSETS_OBJECTS_FOLDER = "objects"

        private const val PING_DELAY = 5000L
    }

}

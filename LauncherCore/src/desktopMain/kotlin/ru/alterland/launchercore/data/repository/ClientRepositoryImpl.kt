package ru.alterland.launchercore.data.repository

import AlterlandLauncher.LauncherCore.BuildConfig
import AlterlandLauncher.LauncherCore.BuildConfig.CLIENT_PROFILES_FOLDER
import AlterlandLauncher.LauncherCore.BuildConfig.SERVER_PROFILES_FOLDER
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
import ru.alterland.launchercore.domain.repository.MojangRepository
import ru.alterland.launchercore.domain.repository.ServerRepository
import ru.alterland.launchercore.util.HashUtils.getCheckSumFromFile
import ru.alterland.launchercore.util.V
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.*


@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
class ClientRepositoryImpl(
    private val applicationIoScope: CoroutineScope,
    private val launchDispatcher: CoroutineDispatcher,
    private val serverRepository: ServerRepository,
    private val launchRepository: LaunchRepository,
    private val mojangRepository: MojangRepository,
    private val clientApi: ClientApi,
    private val jsonReader: Json
): ClientRepository {

    private val jsonWriter = Json {
        prettyPrint = true
    }

    private val workPath = Path(USER_HOME V BuildConfig.WORK_FOLDER)
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

    override fun play(player: Player, serverProfile: ServerProfile) {
        applicationIoScope.launch {
            //client.updateClientStatus(ClientStatus.Verification)

            val locals = mutableMapOf<String, String>()
            val downloads = mutableListOf<ClientProfile.Library>()

            val clientProfile = serverProfile.clientProfile?.let { getClientProfile(it) } ?: return@launch

            val gameArguments = clientProfile.gameArguments.filter { testRules(it.rules) }.flatMap { it.value }
            val jvmArguments = clientProfile.jvmArguments.filter { testRules(it.rules) }.flatMap { it.value }

            workPath.walk().forEach { path ->
                if (!path.startsWith(assetsPath) && path.isRegularFile()) {
                    val checkSum = path.getCheckSumFromFile(hashAlgorithm, 40)
                    val relativePath = path.relativeTo(workPath).toString()
                    locals[relativePath] = checkSum
                }
            }

            clientProfile.libraries.forEach { library ->
                library.downloads?.artifact?.let { artifact ->
                    locals[artifact.path]?.let { checkSum ->
                        if (checkSum != artifact.checkSum) downloads.add(library)
                    }
                }
            }

            println(gameArguments)
            println(jvmArguments)

            println("$locals")
            println("$downloads")
        }

//            val versions = mutableMapOf<String, ClientProfileResponse.DownloadArtifact>()
//            val libraries = mutableMapOf<String, List<ClientProfileResponse.LibraryDownloads>>()
//            val extra = mutableListOf<ClientProfileResponse.DownloadArtifact>()
//            val assets = mutableListOf<ClientProfileResponse.DownloadArtifact>()
//
//            val gameArgs = mutableListOf<String>()
//            val jvmArgs = mutableListOf<String>()
//
//            var clientName: String? = null
//            var assetsIndexName: String? = null
//            var assetsIndexConfig: ClientProfileResponse.DownloadArtifact? = null
//            var mainClass: String? = null
//            var baseVersion: String? = null
//            var versionType: String? = null
//            var authLibInjectorPath: String? = null
//
//            var total = 0L
//
//            gameArgs.add("-XstartOnFirstThread")
//
//            val classPath = mutableListOf<String>()
//
//            profiles.forEach { (_, profile: ClientProfileResponse) ->
//                val profileId = profile.id
//                clientName = profileId
//                if (baseVersion == null) baseVersion = profileId
//
//                // <profile_name>/versions/<profile_name>/<profile_name>.jar
//                profile.downloads?.client?.let {
//                    versions[profileId] = it.copy(
//                        path = "$workFolder/$profileId/$VERSIONS_FOLDER_NAME/$profileId/$profileId.jar"
//                    )
//                }
//
//                // <profile_name>/libraries/*
//                libraries[profileId] =
//                    profile.libraries?.filter { it.downloads?.artifact != null }?.map {
//                        val relativePath = it.downloads!!.artifact!!.path ?: ""
//                        val artifact = it.copy(
//                            downloads = it.downloads.copy(
//                                artifact = it.downloads.artifact!!.copy(
//                                    path = "$workFolder/$profileId/$LIBRARIES_FOLDER_NAME/$relativePath"
//                                )
//                            )
//                        )
//                        if (relativePath.contains("authlibinjector")) {
//                            authLibInjectorPath = artifact.downloads!!.artifact!!.path
//                        }
//                        artifact
//                    } ?: listOf()
//
//                // <client_name>/*
//                val extras = profile.extra?.map {
//                    val relativePath = it.path ?: ""
//                    it.copy(path = "$workFolder/$profileId/$relativePath")
//                } ?: listOf()
//                extra.addAll(extras)
//
//                // assets/indexes/<version_name>.json
//                profile.assetIndex?.let { index ->
//                    profile.assets?.let { assetIndexName ->
//                        assetsIndexName = assetIndexName
//                        assetsIndexConfig = ClientProfileResponse.DownloadArtifact(
//                            sha1 = index.sha1,
//                            path = "$assetIndexName$PROFILE_EXTENSION",
//                            size = index.size,
//                            url = index.url
//                        )
//                    }
//                }
//
//                // arguments
//                profile.arguments?.let { arguments ->
//                    arguments.game?.forEach { element ->
//                        if (element is JsonObject) {
//                            //val rule = json.decodeFromJsonElement<ClientProfileResponse.RulesItem>(element)
//                        } else {
//                            val arg = json.decodeFromJsonElement<String>(element)
//                            jvmArgs.add(arg)
//                        }
//                    }
//                    arguments.jvm?.forEach { element ->
//                        if (element is JsonObject) {
//                            //val rule = json.decodeFromJsonElement<ClientProfileResponse.RulesItem>(element)
//                        } else {
//                            val arg = json.decodeFromJsonElement<String>(element)
//                            gameArgs.add(arg)
//                        }
//                    }
//                }
//
//                profile.type?.let { versionType = it }
//                profile.mainClass?.let { mainClass = it }
//            }
//
//            if (clientName == null) throw Exception("Отсутствует целевой клиент")
//            if (assetsIndexName == null) throw Exception("Отсутствует файл индекса assets")
//            if (assetsIndexConfig == null) throw Exception("Отсутствует конфигурация assets")
//            if (mainClass == null) throw Exception("Отсутствует главный класс")
//
//            val clientPath = "$workFolder/$clientName"
//            val nativesPath = "$workFolder/$baseVersion/$NATIVES_FOLDER_NAME/$baseVersion"
//            val assetIndexesPath = "$workFolder/$ASSETS_INDEXES_FOLDER_NAME"
//            val assetObjectsPath = "$workFolder/$ASSETS_OBJECTS_FOLDER_NAME"
//            val assetsPath = "$workFolder/$ASSETS_FOLDER_NAME"
//
//            val statTime = System.currentTimeMillis()
//            File("$workFolder").walkTopDown().forEach { file ->
//                if (file.isFile) {
//                    val hash = file.getCheckSumFromFile(hashAlgorithm, 40)
//                    println("${file.name} : $hash")
//                }
//            }
//
//            val endTime = System.currentTimeMillis()
//            println("hash time: ${endTime-statTime}")
//
//            client.updateClientDownloadStatus(0, 100000000)
//
//            //download versions
//            versions.forEach { (_, artifact: ClientProfileResponse.DownloadArtifact) ->
//                artifact.path?.let { classPath.add(it) }
//                client.download(listOf(artifact))
//            }
//
//            //download libraries
//            libraries.forEach { (_, libraries: List<ClientProfileResponse.LibraryDownloads>) ->
//                val artifacts = mutableListOf<ClientProfileResponse.DownloadArtifact>()
//                libraries.forEach {
//                    it.downloads?.artifact?.let { artifact ->
//                        artifacts.add(artifact)
//                        artifact.path?.let { classPath.add(it) }
//                    }
//                }
//                client.download(artifacts)
//            }
//
//            client.download(extra) //download extra
//
//            //download assets
////            assetsIndexConfig?.let { artifact ->
////                val path = "$assetIndexesPath/$assetsIndexName$PROFILE_EXTENSION"
////                var file = File(path)
////                if (!file.exists()) {
////                    client.download(listOf(artifact)) //download assetsIndex
////                    file = File(path)
////                }
////                val config = json.decodeFromStream<AssetsIndexResponse>(file.inputStream())
////                config.objects?.let { objects ->
////                    objects.forEach { (_, obj) ->
////                        if (obj.hash != null && obj.hash.length > 2) {
////                            val firstTwo = obj.hash.substring(0, 2)
////                            val url = "${BuildConfig.MOJANG_ASSETS_HOST}/$firstTwo/${obj.hash}"
////
////                            val downloadArtifact = ClientProfileResponse.DownloadArtifact(
////                                sha1 = null,
////                                path = "$firstTwo/${obj.hash}",
////                                size = obj.size,
////                                url = url
////                            )
////                            assets.add(downloadArtifact)
////                        }
////                    }
////                }
////            }
////            client.download(assets)
//
//            println("downloads done")
//
//            //для клиентов без модов
//            val clientFolder = File(clientPath)
//            if (!clientFolder.exists()) {
//                clientFolder.mkdirs()
//            }
//
//            //todo
//            //check server updates again
//
//            client.updateClientStatus(ClientStatus.Launching)
//
//            val gameArguments = clientProfile.gameArguments.filter { it.rules.isEmpty() }.flatMap { it.value }
//            val jvmArguments = clientProfile.jvmArguments.filter { testRules(it.rules) }.flatMap { it.value }
//
//            val options = LaunchOptions(
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
//                launchRepository.launch(options)
//                client.updateClientStatus(ClientStatus.Launched)
//            }
//        }
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

    private fun testRules(rules: List<ClientProfile.Rule>): Boolean {
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
                    ActionRule.DISALLOW -> !osNameMatch && !osArchMatch && !osVersionMatch
                }
            }

            //                  TODO
//                rule.features.forEach {
//
//                }
        }
        return testPass
    }

    private inline fun <reified T> Path.writeObj(obj: T): T {
        jsonWriter.encodeToStream<T>(obj, this.outputStream())
        return jsonReader.decodeFromStream<T>(this.inputStream())
    }

//    private suspend fun Client.download(artifacts: List<ClientProfileResponse.DownloadArtifact>) {
//        artifacts.forEach { artifact ->
//            if (!artifact.url.isNullOrEmpty() && !artifact.path.isNullOrEmpty()) {
//                val filePath = artifact.path
//                val folderPath = filePath.getPath()
//                File(folderPath).mkdirs()
//                val saveFile = File(filePath)
//                saveFile.createNewFile()
//                downloadAndSaveFile(artifact.url, saveFile)
//            }
//        }
//    }

//    private suspend fun getProfiles(
//        rootProfileName: String,
//        rootProfileType: ProfileType
//    ): Map<String, ClientProfileResponse> {
//        val profiles = mutableMapOf<String, ClientProfileResponse>()
//        val rootProfile = getProfile(rootProfileName, rootProfileType)
//        profiles[rootProfileName] = rootProfile
//        if (rootProfile.modules != null) {
//            rootProfile.modules.forEach { module ->
//                val type = ProfileType.fromValue(module.type)
//                if (module.name != null) {
//                    val childModules = getProfiles(module.name, type)
//                    profiles.putAll(childModules)
//                }
//            }
//        }
//        return profiles
//    }

//    private suspend fun getProfile(profileName: String, profileType: ProfileType) = when(profileType) {
//        ProfileType.CUSTOM -> clientApi.getProfile(profileName + PROFILE_EXTENSION)
//        ProfileType.MOJANG -> mojangRepository.getClientProfile(profileName)
//    }
//
//    private suspend fun Client.downloadAndSaveFile(downloadUrl: String, file: File) {
//        clientApi.downloadFile(downloadUrl).execute { httpResponse ->
//            val channel: ByteReadChannel = httpResponse.body()
//            while (!channel.isClosedForRead) {
//                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
//                while (!packet.isEmpty) {
//                    val bytes = packet.readBytes()
//                    file.appendBytes(bytes)
//                    this.updateClientDownloadStatus(bytes.size)
//                }
//            }
//        }
//    }

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

//    private fun Client.updateClientStatus(newStatus: ClientStatus) {
//        val clientsMutable = clients.value.toMutableList()
//        val client = clientsMutable.firstOrNull { it.id == this.id } ?: return
//        val index = clientsMutable.indexOf(client)
//        clientsMutable[index] = client.copy(clientStatus = newStatus)
//        _clients.tryEmit(clientsMutable)
//    }
//
//    private fun Client.updateClientDownloadStatus(newBytesSize: Int, total: Long = 0) {
//        val clientsMutable = clients.value.toMutableList()
//        val client = clientsMutable.firstOrNull { it.id == this.id } ?: return
//        val index = clientsMutable.indexOf(client)
//        val newStatus = if (client.clientStatus is ClientStatus.Downloading) {
//            ClientStatus.Downloading(
//                received = client.clientStatus.received + newBytesSize,
//                total = client.clientStatus.total
//            )
//        } else {
//            ClientStatus.Downloading(
//                received = newBytesSize.toLong(),
//                total = total
//            )
//        }
//        clientsMutable[index] = client.copy(clientStatus = newStatus)
//        _clients.tryEmit(clientsMutable)
//    }

    companion object {
        private val USER_HOME = System.getProperty("user.home")
        private val JVM_DIR = System.getProperty("java.home") V "bin" V "java"
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

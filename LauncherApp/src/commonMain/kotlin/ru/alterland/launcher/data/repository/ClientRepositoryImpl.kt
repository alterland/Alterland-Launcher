//package ru.alterland.launcher.data.repository
//
//import io.ktor.client.call.body
//import io.ktor.utils.io.ByteReadChannel
//import io.ktor.utils.io.readRemaining
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.awaitAll
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.sync.Semaphore
//import kotlinx.coroutines.withContext
//import kotlinx.io.readByteArray
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.decodeFromStream
//import kotlinx.serialization.json.encodeToStream
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.Paths
//import kotlin.io.path.ExperimentalPathApi
//import kotlin.io.path.Path
//import kotlin.io.path.appendBytes
//import kotlin.io.path.deleteExisting
//import kotlin.io.path.deleteIfExists
//import kotlin.io.path.exists
//import kotlin.io.path.inputStream
//import kotlin.io.path.isRegularFile
//import kotlin.io.path.outputStream
//import kotlin.io.path.walk
//
//@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
//class ClientRepositoryImpl(
//    private val dispatcherIo: CoroutineDispatcher,
//    private val applicationScope: CoroutineScope,
//    private val launchRepository: LaunchRepository,
//    private val clientApi: ClientApi,
//    private val jsonReader: Json
//): ClientRepository {
//
//    private val jsonWriter = Json {
//        explicitNulls = false
//    }
//
//    private val workPath = if (BuildConfig.MATCH_LAUNCHER_FOLDER) {
//        Paths.get("").toAbsolutePath()
//    } else {
//        Path("$USER_HOME/${BuildConfig.WORK_FOLDER}")
//    }
//    private val clientProfilesPath = workPath.resolve(CLIENT_PROFILES_FOLDER)
//
//    private val _clientProfiles = MutableStateFlow<List<ClientProfile>>(listOf())
//    override val clientProfiles = _clientProfiles.asStateFlow()
//
//    override suspend fun fetchClientProfile(clientProfileId: String, force: Boolean): ClientProfile? =
//        withContext(dispatcherIo) {
//            val rawProfile = clientApi.getClientProfile(clientProfileId)
//            rawProfile.id?.let { jsonWriter.encodeToStream(rawProfile, clientProfilesPath.resolve(it).outputStream()) }
//            val profile = rawProfile.toDomain(jsonReader)
//            val updateClientProfiles =
//                _clientProfiles.value.toMutableList().apply { kotlin.collections.MutableList.add(profile) }
//            _clientProfiles.emit(updateClientProfiles)
//            profile
//        }
//
//    override fun play(options: Options) {
//        val player = options.player
//        val features = options.features.mapKeys { it.key.value }
//
//        options.clientProfile.updateClientStatus(ClientStatus.Verification)
//
//        applicationScope.launch {
//            val clientProfile = fetchClientProfile(options.clientProfile.id, true) ?: options.clientProfile
//            val isUpdatedSuccessFully = clientProfile.updateClient()
//            if (isUpdatedSuccessFully) {
//                clientProfile.cleanStrictPaths()
//                clientProfile.launch(player, features)
//            }
//        }
//    }
//
//    override fun toggleDownload(clientProfile: ClientProfile) {
//        if (clientProfile.status is ClientStatus.Updating) {
//
//        }
//    }
//
//    private fun ClientProfile.cleanStrictPaths() {
//        strict.forEach {
//            workPath.resolve(it).walk().forEach { path ->
//                val index = downloads.firstOrNull { index -> path.endsWith(index.path) }
//                if (index == null) path.deleteIfExists()
//            }
//        }
//    }
//
//    private suspend fun ClientProfile.updateClient(): Boolean {
//        val externals = getExternalIndexes(externals)
//        val downloads = getDownloads(externals).plus(getDownloads(downloads))
//
//        var received = 0L
//        val total = downloads.sumOf { it.size }
//
//        val errorIndexes = mutableListOf<ClientProfile.DownloadIndex>()
//
//        download(
//            downloads = downloads,
//            onError = { e, downloadIndex ->
//                errorIndexes.add(downloadIndex)
//            }
//        ) { i ->
//            received += i
//            updateClientStatus(ClientStatus.Updating(received = received, total = total))
//        }
//        return if (errorIndexes.isNotEmpty()) {
//            updateClientStatus(ClientStatus.UpdateError(errorIndexes.size))
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun ClientProfile.launch(
//        player: Player,
//        features: Map<String, Boolean>
//    ) {
//        updateClientStatus(ClientStatus.Launching)
//
//        val gameArguments = gameArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
//        val jvmArguments = jvmArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
//        val classPath = downloads.filter { it.classPath && testRules(it.rules) }.map { workPath.resolve(it.path).toString() }
//
//        val launchOptions = LaunchOptions(
//            id = id,
//            gameArguments = gameArguments,
//            jvmArguments = jvmArguments,
//            workPath = workPath,
//            accessToken = player.accessToken,
//            uuid = player.id,
//            nickname = player.nickname,
//            classPath = classPath.joinToString(java.io.File.pathSeparator),
//            mainClass = mainClass
//        )
//        launchRepository.launch(launchOptions)
//        updateClientStatus(ClientStatus.Launched)
//    }
//
//    private suspend fun getExternalIndexes(indexes: List<ClientProfile.ExternalIndex>) = withContext(dispatcherIo) {
//        indexes
//            .filter { testRules(it.rules) }
//            .flatMap { index ->
//                val indexPath = workPath.resolve(index.indexPath)
//                val checkSum = if (indexPath.exists()) indexPath.getCheckSumFromFile() else null
//                if (checkSum == null || index.checkSum != checkSum) {
//                    indexPath.deleteFileAndCreateEmpty()
//                    downloadFile(index.url, indexPath)
//                }
//                val basePath = workPath.resolve(index.externalsPath)
//                when (index.type) {
//                    ExternalIndexType.ASSETS ->
//                        jsonReader.decodeFromStream<AssetsExternalIndexesTypeRaw>(indexPath.inputStream())
//                            .toDomain(basePath)
//
//                    ExternalIndexType.DEFAULT ->
//                        jsonReader.decodeFromStream<List<ClientProfileRaw.DownloadIndex>>(indexPath.inputStream())
//                            .mapNotNull { it.toDomain(basePath) }
//                }
//            }
//    }
//
//    private fun initClientProfiles() {
//        println("Поиск профилей клиентов в папке $clientProfilesPath")
//        val clientProfiles = mutableListOf<ClientProfile>()
//        clientProfilesPath.walk().forEach { path ->
//            if (path.isRegularFile()) {
//                val clientProfileRaw = jsonReader.decodeFromStream<ClientProfileRaw>(path.inputStream())
//                val clientProfile = clientProfileRaw.toDomain(jsonReader)
//                clientProfiles.add(clientProfile)
//            }
//        }
//        _clientProfiles.tryEmit(clientProfiles)
//    }
//
//    private fun Path.removeEmptyParentFolders() {
//        val stream = Files.newDirectoryStream(parent)
//        if (!stream.iterator().hasNext()) {
//            parent.deleteExisting()
//            stream.close()
//            parent.removeEmptyParentFolders()
//        }
//        stream.close()
//    }
//
//    private fun ClientProfile.updateClientStatus(newStatus: ClientStatus) {
//        val clientsMutable = _clientProfiles.value.toMutableList()
//        val client = clientsMutable.firstOrNull { it.id == id } ?: return
//        val index = clientsMutable.indexOf(client)
//        clientsMutable[index] = client.copy(status = newStatus)
//        _clientProfiles.tryEmit(clientsMutable)
//    }
//}
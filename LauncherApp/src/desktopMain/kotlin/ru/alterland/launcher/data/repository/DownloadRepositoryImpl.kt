package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.repository.DownloadRepository

class DownloadRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher
): DownloadRepository {
    //
//    private suspend fun getDownloads(indexes: List<ClientProfile.DownloadIndex>) {
//        indexes
//            .filter { testRules(it.rules) }
//            .filter { index ->
//                val indexPath = workPath.resolve(index.path)
//                val checkSum = if (indexPath.exists()) indexPath.getCheckSumFromFile() else null
//                (checkSum == null || index.checkSum != checkSum && !index.allowChanges).also {
//                    if (it) println("Хеш файла ${indexPath.toAbsolutePath()} отличается.\nОжидается: ${index.checkSum}\nФактически: $checkSum")
//                }
//            }
//    }
//
//    private suspend fun download(
//        downloads: List<ClientProfile.DownloadIndex>,
//        maxConcurrentDownloads: Int = 3,
//        onError: (Exception, ClientProfile.DownloadIndex) -> Unit = { _, _ -> },
//        onBytesReceived: (Int) -> Unit = {}
//    ) = coroutineScope {
//        val semaphore = Semaphore(maxConcurrentDownloads)
//        downloads.map {
//            async {
//                semaphore.acquire()
//                try {
//                    val path = workPath.resolve(it.path)
//                    path.deleteFileAndCreateEmpty()
//                    downloadFile(downloadUrl = it.url, savePath = path, onBytesReceived)
//                } catch (e: Exception) {
//                    onError(e, it)
//                } finally {
//                    semaphore.release()
//                }
//            }
//        }.awaitAll()
//    }
//
//    private suspend fun downloadFile(
//        downloadUrl: String,
//        savePath: Path,
//        onBytesReceived: (Int) -> Unit = {}
//    ) {
//        clientApi.downloadFile(downloadUrl).execute { httpResponse ->
//            val channel: ByteReadChannel = httpResponse.body()
//            while (!channel.isClosedForRead) {
//                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
//                while (!packet.exhausted()) {
//                    val bytes = packet.readByteArray()
//                    savePath.appendBytes(bytes)
//                    onBytesReceived(bytes.size)
//                }
//            }
//        }
//    }
//
//    private fun testRules(
//        rules: List<ClientProfile.Rule>,
//        enabledFeatures: Map<String, Boolean>? = null
//    ): Boolean {
//        var testPass = true
//        for (rule in rules) {
//            testPass = rule.test(USER_OS, enabledFeatures)
//        }
//        return testPass
//    }
//
//    companion object {
//        private val USER_OS = OS(name = OS_NAME, arch = OS_ARCH, version = OS_VERSION)
//    }
    override suspend fun update(clientProfile: ClientProfile) {

    }

    override suspend fun toggleUpdate() {

    }
}

package ru.alterland.launcher.data.source.network

import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.source.network.ktor.httpClient

class DownloadApi(
    private val dispatcherIo: CoroutineDispatcher
) {
    private val httpClient = httpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.HEADERS
        }
        expectSuccess = true
    }

    suspend fun download(url: String) = withContext(dispatcherIo) {
        httpClient.prepareGet(url)
    }
}

package ru.alterland.launcher.data.source.network

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

class SkinsApi(
    private val httpClient: HttpClient,
    private val dispatcherIo: CoroutineDispatcher
) {

}

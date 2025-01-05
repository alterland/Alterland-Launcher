package ru.alterland.launcher.data.source

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.source.network.model.response.ClientProfileObjectResponse

class ClientProfilesApi(
    private val httpClient: HttpClient,
    private val dispatcherIo: CoroutineDispatcher
) {
    suspend fun getClientProfileObjects(): List<ClientProfileObjectResponse> = withContext(dispatcherIo) {
        httpClient.get("$PATH/clientProfiles/objects").body()
    }

    companion object {
        private const val PATH: String = "game"
    }
}

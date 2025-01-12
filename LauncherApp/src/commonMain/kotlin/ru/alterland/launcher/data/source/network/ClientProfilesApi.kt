package ru.alterland.launcher.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.source.network.model.response.ClientProfileObjectResponse
import ru.alterland.launcher.data.source.network.model.response.ClientProfileResponse

class ClientProfilesApi(
    private val httpClient: HttpClient,
    private val dispatcherIo: CoroutineDispatcher
) {
    suspend fun getClientProfile(id: String): ClientProfileResponse = withContext(dispatcherIo) {
        httpClient.get("$PATH/clientProfiles/$id").body()
    }

    suspend fun getClientProfileObjects(): List<ClientProfileObjectResponse> = withContext(dispatcherIo) {
        httpClient.get("$PATH/clientProfiles/objects").body()
    }

    companion object {
        private const val PATH: String = "game"
    }
}

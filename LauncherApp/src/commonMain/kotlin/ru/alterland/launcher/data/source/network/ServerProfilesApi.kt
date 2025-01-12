package ru.alterland.launcher.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.source.network.model.request.ServerProfileRequest
import ru.alterland.launcher.data.source.network.model.response.GetUserResponse
import ru.alterland.launcher.data.source.network.model.response.ServerProfileResponse

class ServerProfilesApi(
    private val httpClient: HttpClient,
    private val dispatcherIo: CoroutineDispatcher
) {
    suspend fun addServerProfile(serverProfile: ServerProfileRequest): ServerProfileResponse =
        withContext(dispatcherIo) {
            httpClient.post("$PATH/serverProfiles") {
                setBody(serverProfile)
            }.body()
        }

    suspend fun editServerProfile(
        serverProfileId: String,
        serverProfile: ServerProfileRequest
    ): ServerProfileResponse = withContext(dispatcherIo) {
        httpClient.patch("$PATH/serverProfiles/$serverProfileId") {
            setBody(serverProfile)
        }.body()
    }

    suspend fun getServerProfiles(): List<ServerProfileResponse> = withContext(dispatcherIo) {
        httpClient.get("$PATH/serverProfiles").body()
    }

    suspend fun getServerProfile(id: String): ServerProfileResponse = withContext(dispatcherIo) {
        httpClient.get("$PATH/serverProfile/$id").body()
    }

    suspend fun getClientProfiles(): GetUserResponse = withContext(dispatcherIo) {
        httpClient.get("$PATH/clientProfiles").body()
    }

    companion object {
        private const val PATH: String = "game"
    }
}
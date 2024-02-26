package ru.alterland.launchercore.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw
import ru.alterland.launchercore.data.source.local.model.ServerProfileRaw

class ClientApi(private val httpClient: HttpClient) {

    suspend fun getServerProfiles(): List<ServerProfileRaw> =
        httpClient.get {
            url {
                path("serverProfiles")
            }
        }.body()

    suspend fun getClientProfile(profileName: String): ClientProfileRaw =
        httpClient.get("clientProfile/$profileName").body()

    suspend fun downloadFile(url: String) = httpClient.prepareGet(url)
}

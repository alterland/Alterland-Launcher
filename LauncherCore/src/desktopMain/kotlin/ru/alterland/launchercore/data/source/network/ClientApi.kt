package ru.alterland.launchercore.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw

class ClientApi(private val httpClient: HttpClient) {

    suspend fun getClientProfile(profileName: String): ClientProfileRaw =
        httpClient.get("clientProfiles/$profileName").body()

    suspend fun downloadFile(url: String) = httpClient.prepareGet(url)
}

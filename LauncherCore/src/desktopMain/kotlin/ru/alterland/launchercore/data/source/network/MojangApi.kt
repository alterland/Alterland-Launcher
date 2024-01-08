package ru.alterland.launchercore.data.source.network

import AlterlandLauncher.LauncherCore.BuildConfig
import ru.alterland.launchercore.data.source.network.model.response.ClientProfileResponse
import ru.alterland.launchercore.data.source.network.model.response.VersionManifestResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class MojangApi(private val httpClient: HttpClient) {

    suspend fun getManifests(): VersionManifestResponse = httpClient.get {
        url {
            protocol = URLProtocol.HTTPS
            host = BuildConfig.MOJANG_MANIFESTS_HOST
            path("mc/game/version_manifest.json")
        }
    }.body()

    suspend fun getClientProfile(url: String): ClientProfileResponse = httpClient.request(url).body()
}

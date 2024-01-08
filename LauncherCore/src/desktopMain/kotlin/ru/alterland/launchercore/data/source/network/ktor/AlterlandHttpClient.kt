package ru.alterland.launchercore.data.source.network.ktor

import AlterlandLauncher.LauncherCore.BuildConfig
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class AlterlandHttpClient(
    private val json: Json
) {
    val client = HttpClient(HttpEngineFactory().createEngine()) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = BuildConfig.CLIENT_API_BASE_URL
            }
            header("Content-Type", "application/json; charset=UTF-8")
        }
        expectSuccess = true
    }
}

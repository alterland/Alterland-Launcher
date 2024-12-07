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
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 120000
            requestTimeoutMillis = 120000
        }
        defaultRequest {
            url {
                protocol = if (BuildConfig.DEV_ENV) URLProtocol.HTTP else URLProtocol.HTTPS
                host = if (BuildConfig.DEV_ENV) BuildConfig.DEV_API_BASE_URL else BuildConfig.PROD_API_BASE_URL
            }
            header("Content-Type", "application/json; charset=UTF-8")
        }
        expectSuccess = true
    }
}

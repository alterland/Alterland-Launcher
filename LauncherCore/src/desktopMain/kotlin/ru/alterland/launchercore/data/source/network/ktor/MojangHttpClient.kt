package ru.alterland.launchercore.data.source.network.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.alterland.launchercore.data.source.network.ktor.HttpEngineFactory

class MojangHttpClient(
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
            connectTimeoutMillis = 15000
            requestTimeoutMillis = 30000
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = ""
            }
            header("Content-Type", "application/json; charset=UTF-8")
        }
        expectSuccess = true
    }
}

package ru.alterland.launcher.data.source.network.ktor

import AlterlandLauncher.ComposeApp.BuildConfig
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.alterland.launcher.AppConfig
import ru.alterland.launcher.util.base.throwAppError

internal expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

internal class AppHttpClient(
    private val json: Json,
    private val cookiesStorage: CustomCookiesStorage
) {
    val client = httpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = cookiesStorage
        }
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            url {
                protocol = if (BuildConfig.DEV_ENV) URLProtocol.HTTP else URLProtocol.HTTPS
                host = AppConfig.apiBaseUrl
            }
            header("Content-Type", "application/json; charset=UTF-8")
        }
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
                throwAppError(json, exceptionResponse.bodyAsText(), exceptionResponse.status.value)
            }
        }
    }
}

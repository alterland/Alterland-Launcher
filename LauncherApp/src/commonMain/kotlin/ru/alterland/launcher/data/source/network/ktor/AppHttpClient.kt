package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.alterland.launcher.AppConfig.apiBaseUrl
import ru.alterland.launcher.BuildConfig
import ru.alterland.launcher.domain.repository.LocalStorage

internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

internal fun configureHttpClient(json: Json, localStorage: LocalStorage) = platformHttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(localStorage.accessToken.value.orEmpty(), null)
            }
            refreshTokens {
                BearerTokens(localStorage.accessToken.value.orEmpty(), null)
            }
        }
    }
    defaultRequest {
        url {
            protocol = if (BuildConfig.DEV_ENV) URLProtocol.HTTP else URLProtocol.HTTPS
            host = apiBaseUrl
        }
        header("Content-Type", "application/json; charset=UTF-8")
    }
    expectSuccess = true
}

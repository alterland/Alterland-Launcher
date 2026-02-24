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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.alterland.launcher.AppConfig.apiBaseUrl
import ru.alterland.launcher.BuildConfig
import ru.alterland.launcher.data.source.network.model.response.GetTokensResponse
import ru.alterland.launcher.domain.repository.LocalStorage

internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

internal fun configureHttpClient(json: Json,
                                 localStorage: LocalStorage,
                                 refreshApiCall: suspend () -> GetTokensResponse
) = platformHttpClient {
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
                val accessToken = localStorage.accessToken.value.orEmpty()
                val refreshToken = localStorage.refreshToken.value.orEmpty()
                BearerTokens(accessToken, refreshToken)
            }
            refreshTokens{
                val currentRefreshToken = localStorage.refreshToken.value?.trim()
                if (currentRefreshToken.isNullOrEmpty()) {
                    runBlocking {
                        localStorage.setAccessToken(null)
                        localStorage.setRefreshToken(null)
                    }
                    return@refreshTokens null
                }

                try {
                    val response = refreshApiCall()
                    val newAccessToken = response.accessToken
                    val newRefreshToken = response.refreshToken
                    localStorage.setAccessToken(newAccessToken)
                    localStorage.setRefreshToken(newRefreshToken)

                    BearerTokens(
                        accessToken = newAccessToken.orEmpty(),
                        refreshToken = newRefreshToken.orEmpty()
                    )
                } catch (e: Exception) {
                    runBlocking {
                        localStorage.setAccessToken(null)
                        localStorage.setRefreshToken(null)
                    }
                    null
                }
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

internal fun configureUploadHttpClient() = platformHttpClient {
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
    expectSuccess = true
}

internal fun configureRefreshHttpClient(json: Json) = platformHttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
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

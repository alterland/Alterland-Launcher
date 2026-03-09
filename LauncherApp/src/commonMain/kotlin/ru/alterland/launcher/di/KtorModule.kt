package ru.alterland.launcher.di

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ktor.configureHttpClient
import ru.alterland.launcher.data.source.network.ktor.configureRefreshHttpClient
import ru.alterland.launcher.data.source.network.ktor.configureUploadHttpClient
import ru.alterland.launcher.domain.repository.LocalStorage

internal const val HTTP_CLIENT_UPLOAD = "uploadHttpClient"
internal const val HTTP_CLIENT_REFRESH = "refreshHttpClient"

internal val ktorModule = module {

    single<HttpClient> {
        val localStorage = get<LocalStorage>()
        val json = get<Json>()
        val refreshClient = get<HttpClient>(named(HTTP_CLIENT_REFRESH))
        configureHttpClient(
            json = json,
            localStorage = localStorage,
            refreshApiCall = {
                val refreshToken = localStorage.refreshToken.value.orEmpty()
                refreshClient.post("/user/refresh") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer $refreshToken")
                }.body()
            }
        )
    }

    single(named(HTTP_CLIENT_UPLOAD)) { configureUploadHttpClient() }
    single(named(HTTP_CLIENT_REFRESH)) {
        val json = get<Json>()
        configureRefreshHttpClient(json = json)
    }
}
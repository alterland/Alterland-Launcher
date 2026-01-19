package ru.alterland.launcher.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ktor.configureHttpClient
import ru.alterland.launcher.data.source.network.ktor.configureUploadHttpClient

internal const val HTTP_CLIENT_UPLOAD = "uploadHttpClient"

internal val ktorModule = module {
    single { configureHttpClient(json = get(), localStorage = get()) }
    single(named(HTTP_CLIENT_UPLOAD)) { configureUploadHttpClient() }
}

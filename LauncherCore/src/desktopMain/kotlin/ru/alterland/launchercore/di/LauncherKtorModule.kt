package ru.alterland.launchercore.di

import io.ktor.client.*
import ru.alterland.launchercore.data.source.network.ktor.AlterlandHttpClient
import ru.alterland.launchercore.data.source.network.ktor.MojangHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val HTTP_CLIENT_ALTERLAND = "alterlandHttpClient"
const val HTTP_CLIENT_MOJANG = "mojangHttpClient"

internal val launcherKtorModule = module {
    single<HttpClient>(named(HTTP_CLIENT_ALTERLAND)) { AlterlandHttpClient(get()).client }
    single<HttpClient>(named(HTTP_CLIENT_MOJANG)) { MojangHttpClient(get()).client }
}

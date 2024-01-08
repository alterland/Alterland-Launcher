package ru.alterland.launchercore.di

import ru.alterland.launchercore.data.source.network.ClientApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launchercore.data.source.network.MojangApi

val launcherApiModule = module {
    single { ClientApi(get(named(HTTP_CLIENT_ALTERLAND))) }
    single { MojangApi(get(named(HTTP_CLIENT_MOJANG))) }
}

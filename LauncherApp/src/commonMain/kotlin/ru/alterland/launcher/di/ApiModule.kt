package ru.alterland.launcher.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ClientProfilesApi
import ru.alterland.launcher.data.source.network.ServerProfilesApi
import ru.alterland.launcher.data.source.network.SkinsApi
import ru.alterland.launcher.data.source.network.UserApi

internal val apiModule = module {
    single { UserApi(httpClient = get(), dispatcherIo = get(named(DISPATCHER_IO))) }
    single { ServerProfilesApi(httpClient = get(), dispatcherIo = get(named(DISPATCHER_IO))) }
    single { ClientProfilesApi(httpClient = get(), dispatcherIo = get(named(DISPATCHER_IO))) }
    single { SkinsApi(httpClient = get(), dispatcherIo = get(named(DISPATCHER_IO))) }
}

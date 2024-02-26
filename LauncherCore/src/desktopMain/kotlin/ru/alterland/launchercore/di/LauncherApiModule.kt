package ru.alterland.launchercore.di

import ru.alterland.launchercore.data.source.network.ClientApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val launcherApiModule = module {
    single { ClientApi(get(named(HTTP_CLIENT_ALTERLAND))) }
}

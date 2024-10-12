package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ktor.AppHttpClient

internal val ktorModule = module {
    single { AppHttpClient(get(), get()).client }
}

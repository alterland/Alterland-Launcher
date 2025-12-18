package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ktor.configureHttpClient

internal val ktorModule = module {
    single { configureHttpClient(json = get(), localStorage = get()) }
}

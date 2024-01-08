package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.ktor.HttpClient

internal val ktorModule = module {
    single { HttpClient(get(), get()).client }
}

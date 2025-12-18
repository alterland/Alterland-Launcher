package ru.alterland.launcher.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DISPATCHER_MAIN = "dispatcherMain"
const val DISPATCHER_IO = "dispatcherIo"
const val DISPATCHER_DEFAULT = "dispatcherDefault"
const val DISPATCHER_UNCONFINED = "dispatcherUnconfined"

const val APPLICATION_SCOPE = "applicationScope"
const val APPLICATION_DEFAULT_SCOPE = "applicationDefaultScope"
const val APPLICATION_IO_SCOPE = "applicationIoScope"
const val APPLICATION_UNCONFINED_SCOPE = "applicationUnconfinedScope"

internal val coroutineModule = module {
    single<CoroutineDispatcher>(named(DISPATCHER_MAIN)) { Dispatchers.Main.immediate }
    single<CoroutineDispatcher>(named(DISPATCHER_IO)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(DISPATCHER_UNCONFINED)) { Dispatchers.Unconfined }

    factory<CoroutineScope>(named(APPLICATION_SCOPE)) { CoroutineScope(Dispatchers.Main.immediate) }
    factory<CoroutineScope>(named(APPLICATION_IO_SCOPE)) { CoroutineScope(Dispatchers.IO) }
    factory<CoroutineScope>(named(APPLICATION_DEFAULT_SCOPE)) { CoroutineScope(Dispatchers.Default) }
    factory<CoroutineScope>(named(APPLICATION_UNCONFINED_SCOPE)) { CoroutineScope(Dispatchers.Unconfined) }
}

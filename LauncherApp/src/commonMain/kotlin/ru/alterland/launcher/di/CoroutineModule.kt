package ru.alterland.launcher.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    single(named(DISPATCHER_MAIN)) { Dispatchers.Main.immediate }
    single(named(DISPATCHER_IO)) { Dispatchers.IO }
    single(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
    single(named(DISPATCHER_UNCONFINED)) { Dispatchers.Unconfined }

    factory(named(APPLICATION_SCOPE)) { CoroutineScope(Dispatchers.Main.immediate) }
    factory(named(APPLICATION_IO_SCOPE)) { CoroutineScope(Dispatchers.IO) }
    factory(named(APPLICATION_DEFAULT_SCOPE)) { CoroutineScope(Dispatchers.Default) }
    factory(named(APPLICATION_UNCONFINED_SCOPE)) { CoroutineScope(Dispatchers.Unconfined) }
}

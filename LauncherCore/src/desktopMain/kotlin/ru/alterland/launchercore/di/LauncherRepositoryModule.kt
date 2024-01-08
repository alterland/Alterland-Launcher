package ru.alterland.launchercore.di

import ru.alterland.launchercore.data.repository.ClientRepositoryImpl
import ru.alterland.launchercore.data.repository.MojangRepositoryImpl
import ru.alterland.launchercore.domain.repository.ClientRepository
import ru.alterland.launchercore.domain.repository.MojangRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launchercore.data.repository.LaunchRepositoryImpl
import ru.alterland.launchercore.data.repository.ServerRepositoryImpl
import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.domain.repository.ServerRepository

internal val launcherRepositoryModule = module {
    single<ServerRepository> { ServerRepositoryImpl(get()) }
    single<ClientRepository> { ClientRepositoryImpl(
        get(named(APPLICATION_IO_SCOPE)), get(named(DISPATCHER_DEFAULT)), get(), get(), get(), get(), get())
    }
    single<LaunchRepository> { LaunchRepositoryImpl() }
    single<MojangRepository> { MojangRepositoryImpl(get(named(DISPATCHER_IO)), get()) }
}

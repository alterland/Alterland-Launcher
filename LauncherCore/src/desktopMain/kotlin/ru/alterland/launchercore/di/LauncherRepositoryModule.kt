package ru.alterland.launchercore.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launchercore.data.repository.ClientRepositoryImpl
import ru.alterland.launchercore.data.repository.LaunchRepositoryImpl
import ru.alterland.launchercore.domain.repository.ClientRepository
import ru.alterland.launchercore.domain.repository.LaunchRepository

internal val launcherRepositoryModule = module {
    single<ClientRepository> { ClientRepositoryImpl(
        get(named(DISPATCHER_IO)), get(named(APPLICATION_SCOPE)), get(), get(), get())
    }
    single<LaunchRepository> { LaunchRepositoryImpl() }
}

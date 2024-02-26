package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.repository.ErrorRepositoryImpl
import ru.alterland.launcher.data.repository.UserRepositoryImpl
import ru.alterland.launcher.data.source.local.LocalStorageImpl
import ru.alterland.launcher.data.source.network.ktor.CustomCookiesStorage
import ru.alterland.launcher.domain.repository.ErrorRepository
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository

internal val repositoryModule = module {
    singleOf(::CustomCookiesStorage)
    singleOf<ErrorRepository>(::ErrorRepositoryImpl)
    single<LocalStorage> { LocalStorageImpl(get(named(DISPATCHER_IO)), get(named(APPLICATION_IO_SCOPE)), get()) }
    single<UserRepository> { UserRepositoryImpl(get(named(DISPATCHER_IO)), get()) }
}

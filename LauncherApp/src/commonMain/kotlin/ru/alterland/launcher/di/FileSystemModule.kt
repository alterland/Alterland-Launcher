package ru.alterland.launcher.di

import kotlinx.io.files.SystemFileSystem
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val fileSystemModule = module {
    singleOf(::SystemFileSystem)
}

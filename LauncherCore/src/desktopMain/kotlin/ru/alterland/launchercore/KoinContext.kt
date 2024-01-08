package ru.alterland.launchercore

import org.koin.dsl.koinApplication
import ru.alterland.launchercore.di.launcherModule

object KoinContext {
    val koin = koinApplication {
        modules(launcherModule)
    }.koin
}

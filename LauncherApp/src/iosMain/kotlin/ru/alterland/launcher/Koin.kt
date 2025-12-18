package ru.alterland.launcher

import org.koin.core.context.startKoin
import ru.alterland.launcher.di.commonModule
import ru.alterland.launcher.di.iosModule

fun initKoin(){
    startKoin {
        modules(commonModule)
        modules(iosModule())
    }
}

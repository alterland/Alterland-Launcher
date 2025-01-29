package ru.alterland.launcher

import ru.alterland.launcher.util.OS

expect class PlatformConfiguration {
    val defaultDir: String
    val os: OS
}

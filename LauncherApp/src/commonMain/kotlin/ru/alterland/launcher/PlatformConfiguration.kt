package ru.alterland.launcher

import ru.alterland.launcher.util.OS

expect class PlatformConfiguration {
    val rootDir: String
    val storeDir: String
    val os: OS
    val osVersion: String
}

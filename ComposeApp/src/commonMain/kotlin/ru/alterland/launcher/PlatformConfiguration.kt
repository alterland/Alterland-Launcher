package ru.alterland.launcher

import ru.alterland.launcher.util.OS

expect class PlatformConfiguration() {
    val os: OS
    val osVersion: String
}

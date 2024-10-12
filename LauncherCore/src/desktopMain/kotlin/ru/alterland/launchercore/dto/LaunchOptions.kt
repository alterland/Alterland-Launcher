package ru.alterland.launchercore.dto

import java.nio.file.Path

data class LaunchOptions(
    val id: String,
    val gameArguments: List<String>,
    val jvmArguments: List<String>,
    val workPath: Path,
    val classPath: String,
    val mainClass: String,
    val accessToken: String,
    val uuid: String,
    val nickname: String
)

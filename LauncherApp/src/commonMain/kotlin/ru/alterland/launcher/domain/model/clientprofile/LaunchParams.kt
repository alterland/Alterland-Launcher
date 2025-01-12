package ru.alterland.launcher.domain.model.clientprofile

import java.nio.file.Path

data class LaunchParams(
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

package ru.alterland.launcher.domain.model.clientprofile

data class LaunchParams(
    val id: String,
    val gameArguments: List<String>,
    val jvmArguments: List<String>,
    val workDir: String,
    val classPath: String,
    val mainClass: String,
    val accessToken: String,
    val uuid: String,
    val nickname: String
)

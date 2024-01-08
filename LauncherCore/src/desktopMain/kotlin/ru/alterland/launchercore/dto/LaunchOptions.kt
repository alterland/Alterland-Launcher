package ru.alterland.launchercore.dto

data class LaunchOptions(
    val gameArguments: List<String>,
    val jvmArguments: List<String>,
    val gameDir: String,
    val jvmDir: String,
    val authLibInjectorPath: String?,
    val nativesDir: String,
    val assetIndex: String,
    val assetsDir: String,
    val classPath: String,
    val mainClass: String,
    val accessToken: String,
    val uuid: String,
    val nickname: String,
    val versionName: String?,
    val versionType: String?,
)

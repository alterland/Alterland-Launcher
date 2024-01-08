package ru.alterland.launchercore.data.repository

import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.dto.LaunchOptions
import java.io.File

class LaunchRepositoryImpl: LaunchRepository {

    override fun launch(options: LaunchOptions) {
        val gameArgsWithValues = addArgValues(options.gameArguments, options)
        val jvmArgsWithValues = addArgValues(options.jvmArguments, options)

        val mainClass = options.mainClass

        val args = mutableListOf<String>()

        args.add(options.jvmDir)
        args.addAll(gameArgsWithValues)
        args.add(mainClass)
        args.addAll(jvmArgsWithValues)

        println("Launch command: ${args.joinToString(" ")}")

        val builder = ProcessBuilder(args)
            .redirectErrorStream(true)
        builder.directory(File(options.gameDir))
        builder.inheritIO()
        builder.start()
    }

    private fun addArgValues(args: List<String>, options: LaunchOptions) = args.map { element ->
        if (element.contains(VALUE_START) && element.contains(VALUE_END)) {
            val startIndex = element.indexOf(VALUE_START)
            val endIndex = element.indexOf(VALUE_END)
            val value = element.substring(startIndex + VALUE_START.length, endIndex)

            val knownValue = ArgValue.fromValue(value)

            val argValue = when(knownValue) {
                ArgValue.AUTH_PLAYER_NAME -> options.nickname
                ArgValue.VERSION_TYPE -> options.versionType
                ArgValue.VERSION_NAME -> options.versionName
                ArgValue.GAME_DIRECTORY -> options.gameDir
                ArgValue.ASSETS_ROOT -> options.assetsDir
                ArgValue.ASSETS_INDEX_NAME -> options.assetIndex
                ArgValue.AUTH_UUID -> options.uuid
                ArgValue.AUTH_ACCESS_TOKEN -> options.accessToken
                ArgValue.USER_TYPE -> "msa"
                ArgValue.NATIVES_DIRECTORY -> options.nativesDir
                ArgValue.LAUNCHER_NAME -> "Alterland Launcher"
                ArgValue.LAUNCHER_VERSION -> "1.0"
                ArgValue.CLASSPATH -> options.classPath
                ArgValue.AUTHLIB_PATH -> options.authLibInjectorPath
                ArgValue.UNKNOWN -> ""
            }

            element
                .replace(VALUE_START, "")
                .replace(VALUE_END, "")
                .replace(value, argValue ?: "")
        } else {
            element
        }
    }

    enum class ArgValue(val value: String) {
        AUTH_PLAYER_NAME("auth_player_name"),
        VERSION_NAME("version_name"),
        GAME_DIRECTORY("game_directory"),
        ASSETS_ROOT("assets_root"),
        ASSETS_INDEX_NAME("assets_index_name"),
        AUTH_UUID("auth_uuid"),
        AUTH_ACCESS_TOKEN("auth_access_token"),
        USER_TYPE("user_type"),
        VERSION_TYPE("version_type"),
        NATIVES_DIRECTORY("natives_directory"),
        LAUNCHER_NAME("launcher_name"),
        LAUNCHER_VERSION("launcher_version"),
        CLASSPATH("classpath"),
        AUTHLIB_PATH("authlib_path"),
        UNKNOWN("");

        companion object {
            fun fromValue(value: String?): ArgValue = ArgValue.entries.firstOrNull {
                it.value.startsWith(value?.lowercase() ?: "")
            } ?: UNKNOWN
        }
    }

    companion object {
        private const val VALUE_START = "\${"
        private const val VALUE_END = "}"
    }
}

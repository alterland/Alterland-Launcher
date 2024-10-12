package ru.alterland.launchercore.data.repository

import ru.alterland.launchercore.domain.repository.LaunchRepository
import ru.alterland.launchercore.dto.LaunchOptions
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.exists

class LaunchRepositoryImpl: LaunchRepository {

    override fun launch(options: LaunchOptions) {
        val gameArgsWithValues = addArgValues(options.gameArguments, options)
        val jvmArgsWithValues = addArgValues(options.jvmArguments, options).toMutableList()

        jvmArgsWithValues.firstOrNull()?.let {
            val jvmPath = options.workPath.resolve(it)
            if (IS_POSIX && jvmPath.exists()) {
                Files.setPosixFilePermissions(jvmPath, BIN_POSIX_PERMISSIONS)
            }
        }

        val args = mutableListOf<String>()
        args.addAll(jvmArgsWithValues)
        args.add(options.mainClass)
        args.addAll(gameArgsWithValues)

        println("Launch command: ${args.joinToString(" ")}")

        val gameDir = options.workPath.resolve(options.id).toFile()
        val builder = ProcessBuilder(args).redirectErrorStream(true)
        builder.directory(gameDir)
        builder.inheritIO()
        builder.start()
    }

    private fun addArgValues(args: List<String>, options: LaunchOptions) = args.map { element ->
        var mutableElement = element
        ArgValue.entries.filter { element.contains(it.value) }.filter { it != ArgValue.UNKNOWN }.forEach {
            val knownValue = ArgValue.fromValue(it.value)

            val argValue = when(knownValue) {
                ArgValue.AUTH_PLAYER_NAME -> options.nickname
                ArgValue.VERSION_TYPE -> "release"
                ArgValue.VERSION_NAME -> options.id
                ArgValue.GAME_DIRECTORY -> options.workPath.resolve(options.id).toString()
                ArgValue.AUTH_UUID -> options.uuid
                ArgValue.AUTH_ACCESS_TOKEN -> options.accessToken
                ArgValue.USER_TYPE -> "msa"
                ArgValue.LAUNCHER_NAME -> "Alterland Launcher"
                ArgValue.LAUNCHER_VERSION -> "1.0"
                ArgValue.CLASSPATH -> options.classPath
                ArgValue.WORK_PATH -> options.workPath.toString()
                ArgValue.CLASSPATH_SEPARATOR -> File.pathSeparator
                ArgValue.UNKNOWN -> ""
            }

            mutableElement = mutableElement.replace(it.value, argValue)
        }
        mutableElement
    }

    enum class ArgValue(val value: String) {
        AUTH_PLAYER_NAME("\${auth_player_name}"),
        VERSION_NAME("\${version_name}"),
        GAME_DIRECTORY("\${game_directory}"),
        AUTH_UUID("\${auth_uuid}"),
        AUTH_ACCESS_TOKEN("\${auth_access_token}"),
        USER_TYPE("\${user_type}"),
        VERSION_TYPE("\${version_type}"),
        LAUNCHER_NAME("\${launcher_name}"),
        LAUNCHER_VERSION("\${launcher_version}"),
        CLASSPATH("\${classpath}"),
        WORK_PATH("\${work_dir}"),
        CLASSPATH_SEPARATOR("\${classpath_separator}"),
        UNKNOWN("");

        companion object {
            fun fromValue(value: String?): ArgValue = ArgValue.entries.firstOrNull {
                it.value.startsWith(value?.lowercase() ?: "")
            } ?: UNKNOWN
        }
    }

    companion object {
        private val IS_POSIX = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")
        private val BIN_POSIX_PERMISSIONS = setOf(
            PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, // Owner
            PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_EXECUTE, // Group
            PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE // Others
        )
    }
}

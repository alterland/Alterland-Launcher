package ru.alterland.launcher.data.repository

import ru.alterland.launcher.data.model.ArgValue
import ru.alterland.launcher.domain.model.clientprofile.LaunchParams
import ru.alterland.launcher.domain.repository.LaunchRepository
import ru.alterland.launcher.util.BIN_POSIX_PERMISSIONS
import ru.alterland.launcher.util.IS_POSIX
import java.io.File
import java.nio.file.Files
import kotlin.io.path.exists

class LaunchRepositoryImpl(): LaunchRepository {

    override fun launch(options: LaunchParams) {
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

    private fun addArgValues(args: List<String>, options: LaunchParams) = args.map { element ->
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
}

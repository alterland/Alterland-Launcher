package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.model.ArgValue
import ru.alterland.launcher.domain.model.clientprofile.*
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.domain.repository.LaunchRepository
import ru.alterland.launcher.util.ClientProfileUtils.testRules
import ru.alterland.launcher.util.extentions.makeExecutable
import ru.alterland.launcher.util.extentions.pathSeparator
import ru.alterland.launcher.util.extentions.v
import java.io.File

class LaunchRepositoryImpl(
    private val fileSystem: FileSystem,
    private val clientProfilesRepository: ClientProfilesRepository,
    private val platformConfiguration: PlatformConfiguration,
    private val dispatcherMain: CoroutineDispatcher
): LaunchRepository {

    override suspend fun launch(
        clientProfile: ClientProfile,
        player: Player,
        features: Map<Feature, Boolean>
    ) {
        clientProfilesRepository.setClientStatus(clientProfile, ClientStatus.Launching)

        val features = features.mapKeys { it.key.value }
        val gameArguments = clientProfile.gameArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
        val jvmArguments = clientProfile.jvmArguments.filter { testRules(it.rules, features) }.flatMap { it.value }
        val classPath = clientProfile.downloads.filter { it.classPath && testRules(it.rules) }.map { "${platformConfiguration.defaultDir}${it.path}" }

        val launchParams = LaunchParams(
            id = clientProfile.id,
            gameArguments = gameArguments,
            jvmArguments = jvmArguments,
            workDir = platformConfiguration.defaultDir,
            accessToken = player.accessToken,
            uuid = player.id,
            nickname = player.nickname,
            classPath = classPath.joinToString(pathSeparator),
            mainClass = clientProfile.mainClass
        )
        withContext(dispatcherMain) {
            launch(launchParams)
            clientProfilesRepository.setClientStatus(clientProfile, ClientStatus.Launched)
        }
    }

    private fun launch(params: LaunchParams) {

        val gameArgsWithValues = addArgValues(params.gameArguments, params)
        val jvmArgsWithValues = addArgValues(params.jvmArguments, params).toMutableList()

        jvmArgsWithValues.firstOrNull()?.let {
            val jvmPath = Path(it)
            with(fileSystem) {
                if (exists(jvmPath)) {
                    jvmPath.makeExecutable()
                }
            }
        }

        val args = mutableListOf<String>()
        args.addAll(jvmArgsWithValues)
        args.add(params.mainClass)
        args.addAll(gameArgsWithValues)

        println("Launch command: ${args.joinToString(" ")}")

        val gameDir = File(params.workDir v params.id)
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
                ArgValue.GAME_DIRECTORY -> options.workDir v options.id
                ArgValue.AUTH_UUID -> options.uuid
                ArgValue.AUTH_ACCESS_TOKEN -> options.accessToken
                ArgValue.USER_TYPE -> "msa"
                ArgValue.LAUNCHER_NAME -> "Alterland Launcher"
                ArgValue.LAUNCHER_VERSION -> "1.0"
                ArgValue.CLASSPATH -> options.classPath
                ArgValue.WORK_DIR -> options.workDir
                ArgValue.CLASSPATH_SEPARATOR -> pathSeparator
                ArgValue.UNKNOWN -> ""
            }

            mutableElement = mutableElement.replace(it.value, argValue)
        }
        mutableElement
    }
}

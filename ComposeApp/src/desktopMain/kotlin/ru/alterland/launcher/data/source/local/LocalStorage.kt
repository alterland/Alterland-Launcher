package ru.alterland.launcher.data.source.local

import AlterlandLauncher.ComposeApp.BuildConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

actual class LocalStorage(
    private val applicationIoScope: CoroutineScope,
    private val dispatcherIo: CoroutineDispatcher
) {

    private val workFolder = "${System.getProperty("user.home")}/${BuildConfig.WORK_FOLDER}"

    private lateinit var storage: Persistent
    private lateinit var file: File

    init {
        getOrCreate()
    }

    actual val settingsFlow = MutableSharedFlow<Map<String, String>>(1, 0, BufferOverflow.DROP_OLDEST)
    actual val cookiesFlow = MutableSharedFlow<Map<String, List<PersistentCookie>>>(1, 0, BufferOverflow.DROP_OLDEST)

    actual fun getSettings(): MutableMap<String, String> = storage.settings

    actual fun storeSetting(key: String, value: Any) {
        storage.settings[key] = value.toString()
        applicationIoScope.launch {
            saveStorage()
            settingsFlow.emit(storage.settings)
        }
    }

    actual fun getString(key: String) = storage.settings[key]

    actual fun getBoolean(key: String) = storage.settings[key]?.toBooleanStrictOrNull()

    actual fun getDouble(key: String) = storage.settings[key]?.toDoubleOrNull()


    actual suspend fun storeCookie(domain: String, cookie: PersistentCookie) {
        val domainStorage = storage.cookies.getOrPut(domain, defaultValue = { listOf() })
        val cookies = domainStorage.filterNot { it.name == cookie.name }.toMutableList()
        if (cookie.value.isNotEmpty()) {
            //if cookie value is empty then it is a remove cookie call
            cookies.add(cookie)
        }
        storage.cookies[domain] = cookies

        if (cookie.name != "access_token") {
            saveStorage()
        } else {
            if (getBoolean(LocalStoreFields.REMEMBER) == true) {
                saveStorage()
            }
        }
        cookiesFlow.emit(storage.cookies)
    }

    actual fun getAllCookies(domain: String) = storage.cookies[domain]?.filter { it.value.isNotEmpty() }

    private fun getOrCreate() = applicationIoScope.launch {
        val directory = File(workFolder)
        file = File("$directory/$FILE_NAME")
        storage = if (!file.exists() || file.isDirectory) {
            directory.mkdirs()
            fillNewFile(file)
        } else {
            readFromStorage(file)
        }
        settingsFlow.emit(storage.settings)
        cookiesFlow.emit(storage.cookies)
    }

    private suspend fun readFromStorage(existingFile: File): Persistent {
        val fileContent = existingFile.readText()
        return try {
            Json.decodeFromString<Persistent>(fileContent)
        } catch (e: SerializationException) {
            migrate(existingFile, fileContent)
        } catch (e: Exception) {
            throw Error(e)
        }
    }

    private suspend fun saveStorage() = withContext(dispatcherIo) {
        val json = Json.encodeToString(storage)
        file.writeText(json)
    }

    private suspend fun migrate(existingFile: File, prevContent: String) = withContext(dispatcherIo) {
        readFallback(prevContent)
        fillNewFile(existingFile)
    }

    private suspend fun fillNewFile(newFile: File): Persistent = withContext(dispatcherIo) {
        val storage = Persistent(
            settings = mutableMapOf<String, String>().apply {
                put(LocalStoreFields.REMEMBER, DEFAULT_REMEMBER_ME.toString())
                put(LocalStoreFields.PATH, workFolder)
                put(LocalStoreFields.RAM, DEFAULT_RAM.toString())
            }
        )

        val json = Json.encodeToString(storage)

        newFile.writeText(json)

        println("Persistent storage created on ${newFile.path}")

        return@withContext readFromStorage(newFile)
    }

    private suspend fun readFallback(prevContent: String) = withContext(dispatcherIo) {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val current = LocalDateTime.now().format(formatter)
        val directory = File(workFolder)
        val file = File("$directory/error_backup_${current}_$FILE_NAME")
        file.writeText(prevContent)
    }

    companion object {
        private const val FILE_NAME = "persistent.json"

        //default launcher settings
        private const val DEFAULT_REMEMBER_ME = true
        private const val DEFAULT_RAM: Double = 4.0 //in GB
    }
}

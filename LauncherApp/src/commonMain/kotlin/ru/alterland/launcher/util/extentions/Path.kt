package ru.alterland.launcher.util.extentions

import io.ktor.utils.io.core.*
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource
import kotlinx.serialization.json.io.encodeToSink
import ru.alterland.launcher.util.hash.StringUtils.encodeHex
import java.security.MessageDigest

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Path.saveJson(fileSystem: FileSystem, json: Json, value: T) {
    fileSystem.sink(this).buffered().use { sink ->
        json.encodeToSink(value = value, sink = sink)
    }
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Path.readJson(fileSystem: FileSystem, json: Json): T {
    return fileSystem.source(this).buffered().use { source ->
        json.decodeFromSource(source)
    }
}

fun Path.checkSum(fileSystem: FileSystem): String {
    val digest = MessageDigest.getInstance("SHA-1")
    fileSystem.source(this).buffered().use { source ->
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var read = source.readAvailable(buffer, 0, DEFAULT_BUFFER_SIZE)
        while (read > 0) {
            digest.update(buffer, 0, read)
            read = source.readAvailable(buffer, 0, DEFAULT_BUFFER_SIZE)
        }
    }
    val bytes = digest.digest()
    val hexCode = encodeHex(bytes, true)
    return String(hexCode)
}

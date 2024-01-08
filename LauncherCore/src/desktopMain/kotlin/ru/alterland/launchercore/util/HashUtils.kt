package ru.alterland.launchercore.util

import java.io.*
import java.math.BigInteger
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.inputStream


object HashUtils {

    private const val STREAM_BUFFER_LENGTH = 65536

    fun Path.getCheckSumFromFile(digest: MessageDigest, hashLength: Int): String {
        val fis = this.inputStream()
        val byteArray = updateDigest(digest, fis).digest()
        fis.close()
        return String.format("%1$0" + hashLength + "x", *arrayOf<Any>(BigInteger(1, byteArray)))
    }

    /**
     * Reads through an InputStream and updates the digest for the data
     *
     * @param digest The MessageDigest to use (e.g. MD5)
     * @param data Data to digest
     * @return the digest
     */
    private fun updateDigest(digest: MessageDigest, data: InputStream): MessageDigest {
        val buffer = ByteArray(STREAM_BUFFER_LENGTH)
        var read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        while (read > -1) {
            digest.update(buffer, 0, read)
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        }
        return digest
    }

}
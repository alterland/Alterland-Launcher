package ru.alterland.launcher.util.hash

import ru.alterland.launcher.util.hash.StringUtils.encodeHex
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

object HashUtils {

    fun getCheckSumFromFile(digest: MessageDigest, filePath: String): String {
        val file = File(filePath)
        return getCheckSumFromFile(digest, file)
    }

    fun getCheckSumFromFile(digest: MessageDigest, file: File): String {
        val fis = FileInputStream(file)
        val byteArray = updateDigest(digest, fis).digest()
        fis.close()
        val hexCode = encodeHex(byteArray, true)
        return String(hexCode)
    }

    /**
     * Reads through an InputStream and updates the digest for the data
     *
     * @param digest The MessageDigest to use (e.g. MD5)
     * @param data Data to digest
     * @return the digest
     */
    private fun updateDigest(digest: MessageDigest, data: InputStream): MessageDigest {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var read = data.read(buffer, 0, DEFAULT_BUFFER_SIZE)
        while (read > -1) {
            digest.update(buffer, 0, read)
            read = data.read(buffer, 0, DEFAULT_BUFFER_SIZE)
        }
        return digest
    }

}

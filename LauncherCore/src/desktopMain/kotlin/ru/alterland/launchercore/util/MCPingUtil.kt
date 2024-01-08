package ru.alterland.launchercore.util

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.regex.Pattern

const val PACKET_HANDSHAKE: Int = 0x00
const val PACKET_STATUS_REQUEST: Int = 0x00
const val PACKET_PING: Int = 0x01
const val STATUS_HANDSHAKE = 1

const val COLOR_CHAR = '\u00A7'
private val STRIP_COLOR_PATTERN: Pattern = Pattern.compile("(?i)$COLOR_CHAR([0-9A-FK-ORX]|#[0-9A-Fa-f]{3,6})")

fun stripColors(input: String?): String? {
    return if (input == null) null else STRIP_COLOR_PATTERN.matcher(input).replaceAll("")
}

@Throws(IOException::class)
fun io(b: Boolean, m: String?) {
    if (b) {
        throw IOException(m)
    }
}

@Throws(IOException::class)
fun DataInputStream.readVarInt(): Int {
    var i = 0
    var j = 0
    while (true) {
        val k = this.readByte().toInt()
        i = i or (k and 0x7F shl j++ * 7)
        if (j > 5) {
            throw RuntimeException("VarInt too big")
        }
        if (k and 0x80 != 128) {
            break
        }
    }
    return i
}

@Throws(IOException::class)
fun DataOutputStream.writeVarInt(value: Int) {
    var paramInt = value
    while (true) {
        if (paramInt and -0x80 == 0) {
            this.writeByte(paramInt)
            return
        }
        this.writeByte(paramInt and 0x7F or 0x80)
        paramInt = paramInt ushr 7
    }
}

package ru.alterland.launcher.util.hash

private const val SHA1_BLOCK_SIZE = 64
private const val SHA1_HASH_SIZE = 20

/**
 * Simple streaming SHA-1 implementation that works across targets.
 */
internal class Sha1Digest {
    private val state = intArrayOf(
        0x67452301,
        0xEFCDAB89.toInt(),
        0x98BADCFE.toInt(),
        0x10325476,
        0xC3D2E1F0.toInt()
    )
    private val block = ByteArray(SHA1_BLOCK_SIZE)
    private var blockSize = 0
    private var processedBytes: Long = 0

    fun update(input: ByteArray, offset: Int = 0, length: Int = input.size) {
        var currentOffset = offset
        var remaining = length

        while (remaining > 0) {
            val toCopy = minOf(remaining, SHA1_BLOCK_SIZE - blockSize)
            input.copyInto(
                destination = block,
                destinationOffset = blockSize,
                startIndex = currentOffset,
                endIndex = currentOffset + toCopy
            )

            blockSize += toCopy
            currentOffset += toCopy
            remaining -= toCopy

            if (blockSize == SHA1_BLOCK_SIZE) {
                processBlock(block)
                processedBytes += SHA1_BLOCK_SIZE
                blockSize = 0
            }
        }
    }

    fun digest(): ByteArray {
        val totalBytes = processedBytes + blockSize

        block[blockSize++] = 0x80.toByte()
        if (blockSize > SHA1_BLOCK_SIZE - 8) {
            block.fill(0, blockSize, SHA1_BLOCK_SIZE)
            processBlock(block)
            processedBytes += SHA1_BLOCK_SIZE
            blockSize = 0
        }

        block.fill(0, blockSize, SHA1_BLOCK_SIZE - 8)
        val bitLength = totalBytes shl 3
        for (i in 0 until 8) {
            block[SHA1_BLOCK_SIZE - 1 - i] = (bitLength ushr (8 * i)).toByte()
        }
        processBlock(block)

        val result = ByteArray(SHA1_HASH_SIZE)
        var i = 0
        while (i < state.size) {
            val word = state[i]
            result[i * 4] = (word ushr 24).toByte()
            result[i * 4 + 1] = (word ushr 16).toByte()
            result[i * 4 + 2] = (word ushr 8).toByte()
            result[i * 4 + 3] = word.toByte()
            i++
        }
        return result
    }

    private fun processBlock(chunk: ByteArray) {
        val w = IntArray(80)
        var i = 0
        while (i < 16) {
            val index = i * 4
            w[i] = ((chunk[index].toInt() and 0xFF) shl 24) or
                ((chunk[index + 1].toInt() and 0xFF) shl 16) or
                ((chunk[index + 2].toInt() and 0xFF) shl 8) or
                (chunk[index + 3].toInt() and 0xFF)
            i++
        }

        while (i < 80) {
            w[i] = (w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16]).rotateLeft(1)
            i++
        }

        var a = state[0]
        var b = state[1]
        var c = state[2]
        var d = state[3]
        var e = state[4]

        i = 0
        while (i < 80) {
            val (f, k) = when (i) {
                in 0..19 -> ((b and c) or (b.inv() and d)) to 0x5A827999
                in 20..39 -> (b xor c xor d) to 0x6ED9EBA1
                in 40..59 -> ((b and c) or (b and d) or (c and d)) to 0x8F1BBCDC.toInt()
                else -> (b xor c xor d) to 0xCA62C1D6.toInt()
            }

            val temp = a.rotateLeft(5) + f + e + k + w[i]
            e = d
            d = c
            c = b.rotateLeft(30)
            b = a
            a = temp
            i++
        }

        state[0] += a
        state[1] += b
        state[2] += c
        state[3] += d
        state[4] += e
    }
}

private fun Int.rotateLeft(distance: Int): Int = (this shl distance) or (this ushr (32 - distance))

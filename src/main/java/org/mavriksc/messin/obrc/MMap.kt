package org.mavriksc.messin.obrc

import java.io.ByteArrayOutputStream
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.TreeMap
import kotlin.math.max
import kotlin.math.min

private val path = Paths.get("D:\\code\\jus-messin\\src\\main\\resources\\obrc\\measurements3.txt")
private const val maxChunk: Long = Int.MAX_VALUE.toLong()
private const val newLine = '\n'.toByte()
private const val semiColon = ';'.toByte()
private const val slashArgh = '\r'.toByte()
private val tree = TreeMap<ByteArray, MeasurementAggregator>(Comparator { a, b ->
    // Lexicographic compare of unsigned bytes
    val len = min(a.size, b.size)
    var i = 0
    while (i < len) {
        val diff = (a[i].toInt() and 0xFF) - (b[i].toInt() and 0xFF)
        if (diff != 0) return@Comparator diff
        i++
    }
    a.size - b.size
})

fun main() {
    val start = System.currentTimeMillis()
    mmap()
    val end = System.currentTimeMillis()
    println("Elapsed time: ${end - start}ms")
}

private fun mmap() {
    FileChannel.open(path, StandardOpenOption.READ).use { channel ->
        val fileSize = channel.size()
        var position = 0L
        val carry = ByteArrayOutputStream(1024)

        fun handleLine(bytes: ByteArray) {
            val split = bytes.indexOfFirst { it == semiColon }
            val stationBytes = bytes.copyOfRange(0, split)
            tree[stationBytes] = tree.getOrDefault(stationBytes, MeasurementAggregator())
                .addMeasurementBytes(bytes.copyOfRange(split + 1, bytes.size))
        }

        while (position < fileSize) {
            val mapSize = min(maxChunk, fileSize - position)
            val buf = channel.map(FileChannel.MapMode.READ_ONLY, position, mapSize)

            while (buf.hasRemaining()) {
                val b = buf.get()
                if (b == newLine) {
                    // Complete line found (handle potential CRLF)
                    val lineBytes = carry.toByteArray()
                    val trimmed = if (lineBytes.isNotEmpty() && lineBytes.last() == slashArgh) {
                        lineBytes.copyOf(lineBytes.size - 1)
                    } else lineBytes
                    handleLine(trimmed)
                    carry.reset()
                } else {
                    carry.write(b.toInt() and 0xFF)
                }
            }
            position += mapSize
        }

        // Handle a final line if file doesn't end with newline
        if (carry.size() > 0) {
            val lineBytes = carry.toByteArray()
            val trimmed = if (lineBytes.isNotEmpty() && lineBytes.last() == slashArgh) {
                lineBytes.copyOf(lineBytes.size - 1)
            } else lineBytes
            handleLine(trimmed)
        }
    }
    println("calcs done converting keys and sorting")
    println(tree.mapKeys { it.key.toString(Charsets.UTF_8) })
}

fun MeasurementAggregator.addMeasurementBytes(bytes: ByteArray): MeasurementAggregator {
    val m = bytes.toString(Charsets.UTF_8).toDouble()
    sum += m
    count++
    min = min(m, min)
    max = max(m, max)
    return this
}


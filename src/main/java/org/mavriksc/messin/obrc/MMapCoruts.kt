package org.mavriksc.messin.obrc

import java.nio.file.Paths
import java.io.ByteArrayOutputStream
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.util.TreeMap
import kotlin.math.min
import kotlin.math.max
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

private val path = Paths.get("D:\\code\\jus-messin\\src\\main\\resources\\obrc\\measurements3.txt")
private const val maxChunk: Long = Int.MAX_VALUE.toLong()
private const val newLine = '\n'.toByte()
private const val semiColon = ';'.toByte()
private const val slashArgh = '\r'.toByte()
private const val minus = '-'.toByte()
private val targetCorutCount = Runtime.getRuntime().availableProcessors() * 2

//improvements left to make.
//3 cursors
// remove branching
fun main() {
    val start = System.currentTimeMillis()
    mmapCoruts()
    val end = System.currentTimeMillis()
    println("Elapsed time: ${end - start}ms")
}

private fun mmapCoruts() {
    FileChannel.open(path, StandardOpenOption.READ).use { channel ->
        val fileSize = channel.size()
        if (fileSize == 0L) {
            println(emptyMap<String, MeasurementAggregator>())
            return
        }
        val chunkSize = min(maxChunk, fileSize / targetCorutCount)

        // Compute newline-aligned chunks by backtracking from tentative ends.
        val chunks = mutableListOf<Pair<Long, Long>>() // (position, size)
        var position = 0L
        while (position < fileSize) {
            val tentativeEnd = min(position + chunkSize, fileSize)
            var end = tentativeEnd
            if (tentativeEnd < fileSize) {
                // Backtrack to last '\n' within a small window
                val windowSize = min(1L shl 7, tentativeEnd - position)
                val backStart = tentativeEnd - windowSize
                val backBuf = channel.map(FileChannel.MapMode.READ_ONLY, backStart, windowSize)
                var lastNL: Long = -1
                var idx = 0
                while (idx < windowSize) {
                    if (backBuf.get(idx.toInt()) == newLine) lastNL = idx.toLong()
                    idx++
                }
                if (lastNL >= 0) {
                    end = backStart + lastNL + 1 // include newline
                } else {
                    println("Couldn't find newline in window, falling back to full scan")
                    val fullScanBuf = channel.map(
                        FileChannel.MapMode.READ_ONLY,
                        position,
                        tentativeEnd - position
                    )
                    var foundAt: Long = -1
                    var j = 0L
                    while (j < fullScanBuf.limit()) {
                        if (fullScanBuf.get(j.toInt()) == newLine) foundAt = j
                        j++
                    }
                    end = if (foundAt >= 0) position + foundAt + 1 else tentativeEnd
                }
            }
            chunks += position to (end - position)
            position = end
        }

        // Build comparator for byte-array keys (lexicographic unsigned compare)
        fun byteArrayComparator() = Comparator<ByteArray> { a, b ->
            val len = min(a.size, b.size)
            var i = 0
            while (i < len) {
                val diff = (a[i].toInt() and 0xFF) - (b[i].toInt() and 0xFF)
                if (diff != 0) return@Comparator diff
                i++
            }
            a.size - b.size
        }

        // Process each chunk in parallel coroutines
        val results = runBlocking(Dispatchers.Default) {
            chunks.map { (startPos, size) ->
                async(Dispatchers.Default) {
                    val localTree = TreeMap<ByteArray, MeasurementAggregatorInt>(byteArrayComparator())
                    val buf = channel.map(FileChannel.MapMode.READ_ONLY, startPos, size)
                    val carry = ByteArrayOutputStream(256)

                    fun handleLine(bytes: ByteArray) {
                        if (bytes.isEmpty()) return
                        val split = bytes.indexOfFirst { it == semiColon }
                        if (split <= 0 || split >= bytes.size - 1) return
                        val stationBytes = bytes.copyOfRange(0, split)
                        val agg = localTree.getOrDefault(stationBytes, MeasurementAggregatorInt())
                        localTree[stationBytes] = agg.addMeasurementInt(bytes.copyOfRange(split + 1, bytes.size))
                    }

                    var i = 0
                    val limit = buf.limit()
                    while (i < limit) {
                        val b = buf.get(i)
                        if (b == newLine) {
                            val lineBytes = carry.toByteArray()
                            val trimmed = if (lineBytes.isNotEmpty() && lineBytes.last() == slashArgh) {
                                lineBytes.copyOf(lineBytes.size - 1)
                            } else lineBytes
                            handleLine(trimmed)
                            carry.reset()
                        } else {
                            carry.write(b.toInt() and 0xFF)
                        }
                        i++
                    }
                    // In theory last chunk ends at newline; still handle any residual safely.
                    if (carry.size() > 0) {
                        val lineBytes = carry.toByteArray()
                        val trimmed = if (lineBytes.isNotEmpty() && lineBytes.last() == slashArgh) {
                            lineBytes.copyOf(lineBytes.size - 1)
                        } else lineBytes
                        handleLine(trimmed)
                    }

                    localTree
                }
            }.awaitAll()
        }

        // Merge local trees
        val global = TreeMap<ByteArray, MeasurementAggregatorInt>(byteArrayComparator())

        for (tree in results) {
            for ((k, v) in tree) {
                val g = global[k]
                if (g == null) {
                    // Copy to avoid sharing mutable aggregators across maps
                    global[k] = MeasurementAggregatorInt(v.min, v.max, v.sum, v.count)
                } else {
                    g.min = min(g.min, v.min)
                    g.max = max(g.max, v.max)
                    g.sum += v.sum
                    g.count += v.count
                }
            }
        }
        println("calcs done converting keys and sorting")
        println(global.mapKeys { it.key.toString(Charsets.UTF_8) })
//        val longestStationKey = global.keys.maxBy { it.size }
//        println("Longest station Name: ${longestStationKey?.toString(Charsets.UTF_8)}")
//        println("Size: ${longestStationKey?.size}")
    }
}

data class MeasurementAggregatorInt(
    var min: Int = Int.MAX_VALUE,
    var max: Int = Int.MIN_VALUE,
    var sum: Int = 0,
    var count: Int = 0
) {
    override fun toString() = "${(min * 0.1).f1()}/${((sum / count) * 0.1).f1()}/${(max * 0.1).f1()}"
}

fun MeasurementAggregatorInt.addMeasurementInt(bytes: ByteArray): MeasurementAggregatorInt {
    var index = 0
    var negative = 1
    var digits = bytes.size - 2
    if (bytes[0] == minus) {
        index = 1
        negative = -1
        digits--
    }
    val temp = if (digits == 2) negative * ((bytes[index] * 100 + bytes[index + 1] * 10 + bytes[index + 3]) - 5328) else
        negative * ((bytes[index] * 10 + bytes[index + 2]) - 528)
    max = max(temp, max)
    min = min(temp, min)
    sum += temp
    count++
    return this
}

fun Double.f1() = "%.1f".format(this)
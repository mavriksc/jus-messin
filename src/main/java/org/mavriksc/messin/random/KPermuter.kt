package org.mavriksc.messin.random

import java.util.PriorityQueue
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.system.measureNanoTime

object KPermuter {
    private const val DEFAULT_N = 6
    private const val DEFAULT_COLUMN_BEAM_WIDTH = 48
    private const val DEFAULT_GRAPH_BEAM_WIDTH = 18
    private const val DEFAULT_PARALLEL_ATTEMPTS = 8

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val n = args.firstOrNull()?.toIntOrNull() ?: DEFAULT_N
        var candidate = IntArray(0)
        val elapsed = measureNanoTime {
            candidate = recursiveSuperpermutation(n)
        }
        val rendered = render(candidate)
        val valid = containsAllPermutations(candidate, n)
        val expected = expectedRecursiveLength(n)
        val columns = rotationColumns(n)
        val greedy = arrangeColumnsGreedy(columns)
        val fixedBeam = arrangeColumnsBeam(columns, DEFAULT_COLUMN_BEAM_WIDTH)

        val rotationDeferred = async {
            timed("rotation-oriented-beam") {
                arrangeOrientedColumnsParallel(columns, DEFAULT_COLUMN_BEAM_WIDTH, DEFAULT_PARALLEL_ATTEMPTS)
            }
        }
        val graphDeferred = async {
            timed("graph-permutation-beam") {
                arrangePermutationGraphParallel(n, DEFAULT_GRAPH_BEAM_WIDTH, DEFAULT_PARALLEL_ATTEMPTS)
            }
        }
        val alternates = awaitAll(rotationDeferred, graphDeferred)

        println("n=$n")
        println("length=${candidate.size}")
        println("expectedLength=$expected")
        println("valid=$valid")
        println("elapsedMs=${elapsed / 1_000_000.0}")
        println("candidate=$rendered")
        println("rotationColumns=${columns.size}")
        println("greedyColumnLength=${greedy.sequence.size}")
        println("fixedBeamColumnLength=${fixedBeam.sequence.size}")
        for (result in alternates) {
            println("${result.name}Length=${result.sequence.size}")
            println("${result.name}Valid=${containsAllPermutations(result.sequence, n)}")
            println("${result.name}Explored=${result.explored}")
            println("${result.name}ElapsedMs=${result.elapsedNanos / 1_000_000.0}")
            println("${result.name}Candidate=${render(result.sequence)}")
        }
    }

    fun recursiveSuperpermutation(n: Int): IntArray {
        require(n > 0) { "n must be positive" }

        var current = intArrayOf(0)
        for (size in 2..n) {
            val blocks = orderedPermutationWindows(current, size - 1).map { previous ->
                val block = IntArray(size * 2 - 1)
                for (i in 0 until size - 1) {
                    block[i] = previous[i]
                    block[i + size] = previous[i]
                }
                block[size - 1] = size - 1
                block
            }
            current = mergeWithBestOverlap(blocks)
        }
        return current
    }

    fun expectedRecursiveLength(n: Int): Int {
        require(n > 0) { "n must be positive" }
        var total = 0
        var factorial = 1
        for (i in 1..n) {
            factorial *= i
            total += factorial
        }
        return total
    }

    fun permutations(n: Int): List<IntArray> {
        require(n > 0) { "n must be positive" }
        val result = ArrayList<IntArray>()
        val used = BooleanArray(n)
        val current = IntArray(n)

        fun visit(depth: Int) {
            if (depth == n) {
                result.add(current.copyOf())
                return
            }
            for (symbol in 0 until n) {
                if (!used[symbol]) {
                    used[symbol] = true
                    current[depth] = symbol
                    visit(depth + 1)
                    used[symbol] = false
                }
            }
        }

        visit(0)
        return result
    }

    fun rotationColumns(n: Int): List<ColumnBlock> {
        require(n > 0) { "n must be positive" }
        if (n == 1) {
            return listOf(ColumnBlock(0, listOf(intArrayOf(0)), intArrayOf(0)))
        }

        val basePermutations = permutations(n - 1)
        return basePermutations.mapIndexed { index, tail ->
            val base = IntArray(n)
            base[0] = 0
            for (i in tail.indices) {
                base[i + 1] = tail[i] + 1
            }

            val rotations = ArrayList<IntArray>(n)
            for (offset in 0 until n) {
                val rotation = IntArray(n)
                for (i in 0 until n) {
                    rotation[i] = base[(i + offset) % n]
                }
                rotations.add(rotation)
            }

            ColumnBlock(index, rotations, cyclicBlock(base))
        }
    }

    fun containsAllPermutations(candidate: IntArray, n: Int): Boolean {
        require(n > 0) { "n must be positive" }
        if (candidate.size < n) return false

        val found = BooleanArray(power(n, n))
        var foundCount = 0
        val expected = factorial(n)
        val seen = BooleanArray(n)

        for (start in 0..candidate.size - n) {
            java.util.Arrays.fill(seen, false)
            var valid = true
            var id = 0
            for (offset in 0 until n) {
                val symbol = candidate[start + offset]
                if (symbol !in 0 until n || seen[symbol]) {
                    valid = false
                    break
                }
                seen[symbol] = true
                id = id * n + symbol
            }
            if (valid && !found[id]) {
                found[id] = true
                foundCount++
                if (foundCount == expected) return true
            }
        }

        return false
    }

    fun overlap(left: IntArray, right: IntArray): Int {
        val max = minOf(left.size, right.size)
        for (size in max downTo 1) {
            var matches = true
            for (i in 0 until size) {
                if (left[left.size - size + i] != right[i]) {
                    matches = false
                    break
                }
            }
            if (matches) return size
        }
        return 0
    }

    fun arrangeColumnsGreedy(columns: List<ColumnBlock>): ColumnArrangement {
        if (columns.isEmpty()) return ColumnArrangement(emptyList(), IntArray(0))

        val overlaps = columnOverlapMatrix(columns)
        val remaining = columns.indices.toMutableSet()
        val order = ArrayList<Int>()
        var current = 0
        order.add(current)
        remaining.remove(current)

        while (remaining.isNotEmpty()) {
            val next = remaining.maxWith(compareBy<Int> { overlaps[current][it] }
                .thenByDescending { -it })!!
            order.add(next)
            remaining.remove(next)
            current = next
        }

        return buildArrangement(order, columns)
    }

    fun arrangeColumnsBeam(columns: List<ColumnBlock>, beamWidth: Int): ColumnArrangement {
        require(beamWidth > 0) { "beamWidth must be positive" }
        if (columns.isEmpty()) return ColumnArrangement(emptyList(), IntArray(0))

        val overlaps = columnOverlapMatrix(columns)
        var states = columns.indices.map {
            BeamState(bitSetWith(it, columns.size), it, intArrayOf(it), columns[it].sequence.size)
        }

        repeat(columns.size - 1) {
            val best = PriorityQueue<BeamState>(compareByDescending<BeamState> { it.length }
                .thenBy { it.order.size })

            for (state in states) {
                for (next in columns.indices) {
                    if (!state.hasUsed(next)) {
                        val extra = columns[next].sequence.size - overlaps[state.last][next]
                        val order = state.order.copyOf(state.order.size + 1)
                        order[order.lastIndex] = next
                        val candidate = BeamState(state.usedWith(next), next, order, state.length + extra)
                        best.add(candidate)
                        if (best.size > beamWidth) {
                            best.poll()
                        }
                    }
                }
            }

            states = best.toList()
        }

        val winner = states.minBy { it.length }!!
        return buildArrangement(winner.order.toList(), columns)
    }

    fun arrangeOrientedColumnsParallel(
        columns: List<ColumnBlock>,
        beamWidth: Int,
        attempts: Int
    ): SearchResult = runBlocking {
        require(attempts > 0) { "attempts must be positive" }
        (0 until attempts).map { attempt ->
            async {
                arrangeOrientedColumnsBeam(columns, beamWidth, attempt, attempts)
            }
        }.awaitAll().minBy { it.sequence.size }
    }

    fun arrangeOrientedColumnsBeam(
        columns: List<ColumnBlock>,
        beamWidth: Int,
        startOffset: Int = 0,
        startStride: Int = 1
    ): SearchResult {
        require(beamWidth > 0) { "beamWidth must be positive" }
        require(startStride > 0) { "startStride must be positive" }
        if (columns.isEmpty()) return SearchResult("rotation-oriented-beam", IntArray(0), 0L, 0L)

        val oriented = orientedColumnBlocks(columns)
        val orientationCount = columns.first().orientations.size
        val overlaps = sequenceOverlapMatrix(oriented.map { it.sequence })
        var explored = 0L
        var states = oriented.indices
            .filter { oriented[it].column % startStride == startOffset }
            .map {
                OrientedBeamState(
                    bitSetWith(oriented[it].column, columns.size),
                    oriented[it].column,
                    it,
                    intArrayOf(it),
                    oriented[it].sequence.size
                )
            }
        if (states.isEmpty()) {
            states = listOf(
                OrientedBeamState(
                    bitSetWith(0, columns.size),
                    0,
                    0,
                    intArrayOf(0),
                    oriented[0].sequence.size
                )
            )
        }

        repeat(columns.size - 1) {
            val best = PriorityQueue<OrientedBeamState>(compareByDescending<OrientedBeamState> { it.length }
                .thenBy { it.order.size })

            for (state in states) {
                for (nextColumn in columns.indices) {
                    if (!state.hasUsed(nextColumn)) {
                        for (orientation in 0 until orientationCount) {
                            val next = nextColumn * orientationCount + orientation
                            val extra = oriented[next].sequence.size - overlaps[state.lastOriented][next]
                            val order = state.order.copyOf(state.order.size + 1)
                            order[order.lastIndex] = next
                            best.add(
                                OrientedBeamState(
                                    state.usedWith(nextColumn),
                                    nextColumn,
                                    next,
                                    order,
                                    state.length + extra
                                )
                            )
                            explored++
                            if (best.size > beamWidth) {
                                best.poll()
                            }
                        }
                    }
                }
            }

            states = best.toList()
        }

        val winner = states.minBy { it.length }
        return SearchResult("rotation-oriented-beam", buildSequence(winner.order.map { oriented[it].sequence }), explored, 0L)
    }

    fun arrangePermutationGraphParallel(
        n: Int,
        beamWidth: Int,
        attempts: Int
    ): SearchResult = runBlocking {
        require(attempts > 0) { "attempts must be positive" }
        val perms = permutations(n)
        val overlaps = sequenceOverlapMatrix(perms)
        (0 until attempts).map { attempt ->
            async {
                arrangePermutationGraphBeam(perms, overlaps, beamWidth, attempt, attempts)
            }
        }.awaitAll().minBy { it.sequence.size }
    }

    fun arrangePermutationGraphBeam(
        permutations: List<IntArray>,
        overlaps: Array<IntArray>,
        beamWidth: Int,
        startOffset: Int = 0,
        startStride: Int = 1
    ): SearchResult {
        require(beamWidth > 0) { "beamWidth must be positive" }
        require(startStride > 0) { "startStride must be positive" }
        if (permutations.isEmpty()) return SearchResult("graph-permutation-beam", IntArray(0), 0L, 0L)

        var explored = 0L
        var states = permutations.indices
            .filter { it % startStride == startOffset }
            .map { GraphBeamState(bitSetWith(it, permutations.size), it, intArrayOf(it), permutations[it].size) }
        if (states.isEmpty()) {
            states = listOf(GraphBeamState(bitSetWith(0, permutations.size), 0, intArrayOf(0), permutations[0].size))
        }

        repeat(permutations.size - 1) {
            val best = PriorityQueue<GraphBeamState>(compareByDescending<GraphBeamState> { it.length }
                .thenBy { it.order.size })

            for (state in states) {
                for (next in permutations.indices) {
                    if (!state.hasUsed(next)) {
                        val extra = permutations[next].size - overlaps[state.last][next]
                        val order = state.order.copyOf(state.order.size + 1)
                        order[order.lastIndex] = next
                        best.add(GraphBeamState(state.usedWith(next), next, order, state.length + extra))
                        explored++
                        if (best.size > beamWidth) {
                            best.poll()
                        }
                    }
                }
            }

            states = best.toList()
        }

        val winner = states.minBy { it.length }
        return SearchResult("graph-permutation-beam", buildSequence(winner.order.map { permutations[it] }), explored, 0L)
    }

    fun render(sequence: IntArray): String {
        val builder = StringBuilder(sequence.size)
        for (symbol in sequence) {
            builder.append(('A'.toInt() + symbol).toChar())
        }
        return builder.toString()
    }

    fun columnOverlapMatrix(columns: List<ColumnBlock>): Array<IntArray> {
        return Array(columns.size) { left ->
            IntArray(columns.size) { right ->
                if (left == right) columns[left].sequence.size else overlap(columns[left].sequence, columns[right].sequence)
            }
        }
    }

    fun sequenceOverlapMatrix(sequences: List<IntArray>): Array<IntArray> {
        return Array(sequences.size) { left ->
            IntArray(sequences.size) { right ->
                if (left == right) sequences[left].size else overlap(sequences[left], sequences[right])
            }
        }
    }

    private fun orderedPermutationWindows(sequence: IntArray, n: Int): List<IntArray> {
        val result = ArrayList<IntArray>()
        val found = BooleanArray(power(n, n))
        val seen = BooleanArray(n)

        for (start in 0..sequence.size - n) {
            java.util.Arrays.fill(seen, false)
            var valid = true
            var id = 0
            for (offset in 0 until n) {
                val symbol = sequence[start + offset]
                if (symbol !in 0 until n || seen[symbol]) {
                    valid = false
                    break
                }
                seen[symbol] = true
                id = id * n + symbol
            }
            if (valid && !found[id]) {
                found[id] = true
                result.add(sequence.copyOfRange(start, start + n))
            }
        }

        return result
    }

    private fun mergeWithBestOverlap(blocks: List<IntArray>): IntArray {
        if (blocks.isEmpty()) return IntArray(0)
        var result = blocks.first()
        for (i in 1 until blocks.size) {
            result = appendWithOverlap(result, blocks[i])
        }
        return result
    }

    private fun appendWithOverlap(left: IntArray, right: IntArray): IntArray {
        val shared = overlap(left, right)
        val result = left.copyOf(left.size + right.size - shared)
        for (i in shared until right.size) {
            result[left.size + i - shared] = right[i]
        }
        return result
    }

    private fun cyclicBlock(base: IntArray): IntArray {
        val sequence = IntArray(base.size * 2 - 1)
        for (i in sequence.indices) {
            sequence[i] = base[i % base.size]
        }
        return sequence
    }

    private fun orientedColumnBlocks(columns: List<ColumnBlock>): List<OrientedColumnBlock> {
        val result = ArrayList<OrientedColumnBlock>()
        for (column in columns) {
            for (orientation in column.orientations.indices) {
                result.add(OrientedColumnBlock(column.id, orientation, cyclicBlock(column.orientations[orientation])))
            }
        }
        return result
    }

    private fun buildSequence(sequences: List<IntArray>): IntArray {
        if (sequences.isEmpty()) return IntArray(0)
        var result = sequences.first()
        for (i in 1 until sequences.size) {
            result = appendWithOverlap(result, sequences[i])
        }
        return result
    }

    private fun timed(name: String, block: () -> SearchResult): SearchResult {
        var result: SearchResult
        val elapsed = measureNanoTime {
            result = block()
        }
        return result.copy(name = name, elapsedNanos = elapsed)
    }

    private fun buildArrangement(order: List<Int>, columns: List<ColumnBlock>): ColumnArrangement {
        val sequences = order.map { columns[it].sequence }
        return ColumnArrangement(order, mergeWithBestOverlap(sequences))
    }

    private fun factorial(n: Int): Int {
        var result = 1
        for (i in 2..n) result *= i
        return result
    }

    private fun power(base: Int, exponent: Int): Int {
        var result = 1
        repeat(exponent) {
            result *= base
        }
        return result
    }

    private fun bitSetWith(index: Int, size: Int): LongArray {
        val words = LongArray((size + 63) / 64)
        words[index / 64] = words[index / 64] or (1L shl (index % 64))
        return words
    }
}

data class ColumnBlock(
    val id: Int,
    val orientations: List<IntArray>,
    val sequence: IntArray
)

data class ColumnArrangement(
    val order: List<Int>,
    val sequence: IntArray
)

data class SearchResult(
    val name: String,
    val sequence: IntArray,
    val explored: Long,
    val elapsedNanos: Long
)

private data class OrientedColumnBlock(
    val column: Int,
    val orientation: Int,
    val sequence: IntArray
)

private data class OrientedBeamState(
    val usedMask: LongArray,
    val lastColumn: Int,
    val lastOriented: Int,
    val order: IntArray,
    val length: Int
) {
    fun hasUsed(index: Int): Boolean {
        return (usedMask[index / 64] and (1L shl (index % 64))) != 0L
    }

    fun usedWith(index: Int): LongArray {
        val next = usedMask.copyOf()
        next[index / 64] = next[index / 64] or (1L shl (index % 64))
        return next
    }
}

private data class GraphBeamState(
    val usedMask: LongArray,
    val last: Int,
    val order: IntArray,
    val length: Int
) {
    fun hasUsed(index: Int): Boolean {
        return (usedMask[index / 64] and (1L shl (index % 64))) != 0L
    }

    fun usedWith(index: Int): LongArray {
        val next = usedMask.copyOf()
        next[index / 64] = next[index / 64] or (1L shl (index % 64))
        return next
    }
}

private data class BeamState(
    val usedMask: LongArray,
    val last: Int,
    val order: IntArray,
    val length: Int
) {
    fun hasUsed(index: Int): Boolean {
        return (usedMask[index / 64] and (1L shl (index % 64))) != 0L
    }

    fun usedWith(index: Int): LongArray {
        val next = usedMask.copyOf()
        next[index / 64] = next[index / 64] or (1L shl (index % 64))
        return next
    }
}

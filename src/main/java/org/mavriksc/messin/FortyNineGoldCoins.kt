package org.mavriksc.messin

import org.mavriksc.messin.objects.CombinationGenerator

fun main() {
//    println(intArrayOf(28, 27, 26, 25, 24, 23, 22).sum())
//    println(intArrayOf(31, 30, 29, 21, 20, 19).sum())
//    println((1..49).sum() / 7)
//    println((1..21).sum())
//    println((29..49).sum())
//    println(136 + 38)
    val lol = CombinationGenerator((1..49).toList(), 7)
        .filter { it.sum() == 175 }
        .map { it.toSet() }
    findMutuallyExclusiveGroup(lol, 7)?.forEach { println(it) }
}

/**
 * Find one combination of [count] sets that are mutually exclusive (share no elements).
 *
 * Returns the first valid combination found in input order, or null if none exists.
 */
fun <T> findMutuallyExclusiveGroup(sets: List<Set<T>>, count: Int): List<Set<T>>? {
    require(count >= 0) { "count must be non-negative" }
    if (count == 0) return emptyList()
    if (sets.isEmpty() || count > sets.size) return null

    val currentIndices = IntArray(count)

    fun backtrack(startIndex: Int, depth: Int, used: Set<T>): Boolean {
        if (depth == count) return true

        var i = startIndex
        while (i <= sets.size - (count - depth)) {
            val candidateSet = sets[i]
            if (used.intersect(candidateSet).isEmpty()) {
                currentIndices[depth] = i
                if (backtrack(i + 1, depth + 1, used + candidateSet)) {
                    return true
                }
            }
            i++
        }
        return false
    }

    return if (backtrack(0, 0, emptySet())) {
        currentIndices.map { sets[it] }
    } else {
        null
    }
}

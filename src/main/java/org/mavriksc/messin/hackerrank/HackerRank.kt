package org.mavriksc.messin.hackerrank

import kotlin.math.*

fun main() {
    climbingLeaderboard(arrayOf(1), arrayOf(1,1)).forEach { println(it) }
}

fun designerPdfViewer(h: Array<Int>, word: String) = word.length * word.toLowerCase().chars().map { h[it - 'a'.toInt()] }.max().asInt

fun bonAppetit(bill: Array<Int>, k: Int, b: Int) {
    val fair = (bill.sum() - bill[k]) / 2 - b
    if (fair == 0) println("Bon Appetit") else println(fair)
}

fun pageCount(n: Int, p: Int) = min(p / 2, if (n % 2 == 0) ((n - p) + 1) / 2 else (n - p) / 2)

fun countingValleys(n: Int, s: String): Int {
    var height = 0
    var valCount = 0
    var oldHeight = 0
    for (c in s) {
        when (c) {
            'U' -> height++
            'D' -> height--
        }
        if (height == 0 && oldHeight < 0)
            valCount++
        oldHeight = height
    }
    return valCount
}

fun getMoneySpent(keyboards: Array<Int>, drives: Array<Int>, b: Int) =
        keyboards.flatMap { k -> drives.map { it + k } }.filter { it <= b }.maxWithLimit(b) ?: -1


fun catAndMouse(x: Int, y: Int, z: Int): String {
    val distComp = abs(x - z) - abs(y - z)
    return when {
        distComp > 0 -> "Cat A"
        distComp < 0 -> "Cat B"
        else -> "Mouse C"
    }
}

fun formingMagicSquare(s: Array<Array<Int>>): Int {
    operator fun Array<Array<Int>>.minus(other: Array<Array<Int>>): Int {
        var tot = 0;
        for (y in this.indices) {
            for (x in this[y].indices) {
                tot += abs(this[x][y] - other[x][y])
            }
        }
        return tot
    }

    fun Array<Array<Int>>.transpose(): Array<Array<Int>> {
        val result = Array(this[0].size) { Array(this.size) { 0 } }
        for (y in this.indices) {
            for (x in this[y].indices) {
                result[x][y] = this[y][x]
            }
        }
        return result
    }

    fun Array<Array<Int>>.rotateR(): Array<Array<Int>> {
        val rows = this.size
        val cols = this[0].size
        val result = Array(cols) { Array(rows) { 0 } }
        for (y in this.indices) {
            for (x in this[y].indices) {
                result[y][(cols - 1) - x] = this[x][y]
            }
        }
        return result
    }

    var magicSq = arrayOf(arrayOf(2, 9, 4), arrayOf(7, 5, 3), arrayOf(6, 1, 8))
    val sqList = mutableListOf<Array<Array<Int>>>()

    for (n in 1..4) {
        if (n != 1)
            magicSq = magicSq.rotateR()
        sqList.add(magicSq)
        sqList.add(magicSq.transpose())
    }

    return sqList.map { it - s }.min()!!
}

fun <T : Comparable<T>> Iterable<T>.maxWithLimit(limit: T): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var max = iterator.next()
    while (iterator.hasNext()) {
        val e = iterator.next()
        if (e == limit) return e
        if (max < e && e < limit) max = e
    }
    return max
}

fun pickingNumbers(a: Array<Int>): Int {
    val numCountMap = a.groupingBy { it }.eachCount()
    return numCountMap.map { it.value + max(numCountMap[it.key - 1] ?: 0, numCountMap[it.key + 1] ?: 0) }.max()!!
}

//7
//100 100 50 40 40 20 10
//4
//5 25 50 120

fun climbingLeaderboard(scores: Array<Int>, alice: Array<Int>): Array<Int> {
    // 12PT solution times out on 4 tests.
    val out = Array<Int>(alice.size) { 0 }
    val disScores = scores.distinct()
    var lastScoreIndex = disScores.size

    for ((i, n) in alice.withIndex()) {
        if (lastScoreIndex == 0) {
            out[i] = 1
        } else {
            var found = false
            do {
                if (disScores[lastScoreIndex - 1] > n) {
                    out[i] = lastScoreIndex + 1
                    found = true
                } else if (disScores[lastScoreIndex - 1] == n) {
                    out[i] = lastScoreIndex
                    found = true
                } else {
                    lastScoreIndex--
                }
            } while (lastScoreIndex > 1 && !found)
            if (!found) out[i] = 1
        }
    }
    return out
}


package org.mavriksc.messin.hackerrank

import java.io.File
import java.math.BigInteger
import java.util.*
import java.util.stream.Stream
import kotlin.math.*
import kotlin.random.Random

fun main() {
    println(appendAndDelete("ashle","ash",2))

}

fun readFile(path: String): List<String> {
    return File(path).readLines()
}

//https://www.hackerrank.com/challenges/append-and-delete/problem
fun appendAndDelete(s: String, t: String, k: Int): String {
    // Write your code here
    return if (abs(s.length - t.length) > k)
        "No"
    else {
        // ERROR: issue is that it needs to be from s->t only not just either string
            // actually the problem is symmetric they just have problems with tier tests rip

        // from the start of both strings compare until the first difference found.
        // this will have to be eliminated to match the strings
        // if the # of chars in both strings to the right of this position is > k no otherwise yes.
        val shortStringLen = min(s.length, t.length)
        var index = 0
        while (index < shortStringLen && s[index] == t[index]) {
           index++
        }
        if (s.length-index + t.length-index > k)
            "No"
        else
            "Yes"
    }

}

fun designerPdfViewer(h: Array<Int>, word: String) =
    word.length * word.toLowerCase().chars().map { h[it - 'a'.toInt()] }.max().asInt

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
                when {
                    disScores[lastScoreIndex - 1] > n -> {
                        out[i] = lastScoreIndex + 1
                        found = true
                    }
                    disScores[lastScoreIndex - 1] == n -> {
                        out[i] = lastScoreIndex
                        found = true
                    }
                    else -> lastScoreIndex--
                }
            } while (lastScoreIndex >= 1 && !found)
            if (!found) out[i] = 1
        }
    }
    return out

}

fun getCLBINPUT(lines: List<String>): Pair<Array<Int>, Array<Int>> {
    val first = lines[1].trim().split(" ").map { it.toInt() }.toTypedArray()
    val second = lines[3].trim().split(" ").map { it.toInt() }.toTypedArray()
    return Pair(first, second)
}

fun utopianTree(n: Int): Int {
    if (n == 0) return 1
    var height = 1
    for (x in 1..n) {
        when {
            x % 2 != 0 -> {
                height *= 2
            }
            x % 2 == 0 -> {
                height += 1
            }
        }
    }
    return height
}

fun angryProfessor(k: Int, a: Array<Int>): String {
    return if (a.filter { it <= 0 }.count() >= k) "NO" else "YES"
}

fun beautifulDays(i: Int, j: Int, k: Int): Int =
    (i..j).filter { abs(it - it.toString().reversed().toInt()) % k == 0 }.count()

fun viralAdvertising(n: Int): Int {
    val days = Array<Int>(n) { 0 }
    days[0] = 2
    for (x in 2..n) {
        days[x - 1] = (days[x - 2] * 3 / 2.0).toInt()
    }
    return days.sum()
}

fun saveThePrisoner(n: Int, m: Int, s: Int): Int {
    val pos = (((s - 1) + m) % n)
    return if (pos == 0) n else pos
}

fun circularArrayRotation(a: Array<Int>, k: Int, queries: Array<Int>): Array<Int> {
    return queries.map { a[Math.floorMod((it - k), a.size)] }.toTypedArray()
}

fun permutationEquation(p: Array<Int>): Array<Int> {
    return (1..p.size).map { p.indexOf(p.indexOf(it) + 1) + 1 }.toTypedArray()
}

fun jumpingOnClouds(c: Array<Int>, k: Int): Int {
    var pos = 0
    var ernrg = 100
    do {
        pos = (pos + k) % c.size
        if (c[pos] == 0) ernrg-- else ernrg -= 3
    } while (pos != 0)
    return ernrg
}

fun findDigits(n: Int): Int {
    fun Int.digitStream(): Stream<Int> {
        var num = this
        val things = mutableListOf<Int>()
        do {
            things.add(num % 10)
            num /= 10
        } while (num > 0)
        return things.stream()
    }

    val divMap = mutableMapOf<Int, Boolean>(1 to true, 0 to false);

    return n.digitStream()
        .filter { divMap.computeIfAbsent(it) { key -> n % key == 0 } }
        .count().toInt()
}


fun sortLists() {
    val lists =
        listOf<MutableList<Long>>(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
    Random.nextInt(lists.size)
    (1..144000).forEach { _ -> lists[Random.nextInt(lists.size)].add(Random.nextLong()) }
    val start = Date()
    lists.forEach { it.sort() }
    val end = Date()
    println(end.time - start.time)


}

fun extraLongFactorials(n: Int): Unit {
    var x = BigInteger.ONE;
    for (i in 1..n) {
        x *= i.toBigInteger()
    }
    print(x)
}

//https://www.hackerrank.com/challenges/non-divisible-subset/problem
fun nonDivisibleSubset(k: Int, s: Array<Int>): Int {
    val modMap: Map<Int, List<Int>> = s.groupBy { it % k }
    var size = if (modMap[0].isNullOrEmpty()) 0 else 1;
    for (x in 1..k / 2) {
        size += if (x == k / 2 && k % 2 == 0)
            if (modMap[x].isNullOrEmpty()) 0 else 1;
        else {
            fun len(i: Int) = modMap[i]?.size ?: 0
            max(len(x), len(k - x))
        }
    }
    return size
}


fun timeInWords(h: Int, m: Int): String {
    val words = arrayOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "ten",
        "eleven",
        "twelve",
        "thirteen",
        "fourteen",
        "fifteen",
        "sixteen",
        "seventeen",
        "eighteen",
        "nineteen",
        "twenty",
        "twenty one",
        "twenty two",
        "twenty three",
        "twenty four",
        "twenty five",
        "twenty six",
        "twenty seven",
        "twenty eight",
        "twenty nine"
    )
    val mins = if (m == 1 || m == 59) "minute" else "minutes"
    return when (m) {
        0 -> "${words[h - 1]} o' clock"
        15 -> "quarter past ${words[h - 1]}"
        30 -> "half past ${words[h - 1]}"
        45 -> "quarter to ${words[h]}"
        in 1..29 -> "${words[m - 1]} $mins past ${words[h - 1]}"
        in 31..59 -> "${words[(60 - m) - 1]} $mins to ${words[h]}"
        else -> ""
    }

}

//https://www.hackerrank.com/challenges/happy-ladybugs/problem
fun happyLadybugs(b: String): String {
    val map = b.mapIndexed { index, c -> index to c }.groupBy({ it.second }, { it.first })
    if (b.length == 1 && b[0] != '_') return "NO"
    if (map.containsKey('_'))
    //can swap
        return if (map.all { it.key == '_' || it.value.count() > 1 })
            "YES"
        else "NO"
    else {
        //needs to be in culsters already
        var lastChar = b[0]
        var count = 1
        b.forEach {
            when {
                it == lastChar -> count++
                count < 2 -> return "NO"
                else -> {
                    count = 1
                    lastChar = it
                }
            }
        }
        return if (count > 1) "YES"
        else "NO"
    }
}

fun workbook(n: Int, k: Int, arr: Array<Int>): Int {
    var pageCount = 0
    return arr.map { i ->
        val pagesInChapter = (i - 1) / k + 1
        val specialPagesInChap = (1..pagesInChapter).map {
            val page = pageCount + it
            val lastPossibleProblemOnPage = it * k
            val firstProblemOnPage = lastPossibleProblemOnPage - (k - 1)
            val actualLastProblemOnPage = min(i, lastPossibleProblemOnPage)
            if (page in firstProblemOnPage..actualLastProblemOnPage) 1 else 0
        }.sum()
        pageCount += pagesInChapter
        specialPagesInChap
    }.sum()
}

//https://www.hackerrank.com/challenges/cavity-map/problem
fun cavityMap(grid: Array<String>): Array<String> {
    (1..grid.size - 2).forEach { x ->
        (1..grid.size - 2).forEach { y ->
            val thing = grid[x][y]
            val around = listOf(grid[x - 1][y], grid[x][y - 1], grid[x + 1][y], grid[x][y + 1])
            if (around.all { it < thing })
                grid[x] = grid[x].replaceRange(y, y + 1, "X")
        }
    }
    return grid
}


//https://www.hackerrank.com/challenges/library-fine/problem
fun libraryFine(d1: Int, m1: Int, y1: Int, d2: Int, m2: Int, y2: Int): Int {
    return when {
        y1 > y2 -> (y1 - y2) * 10000
        m1 > m2 && y1 == y2 -> (m1 - m2) * 500
        d1 > d2 && m1 == m2 && y1 == y2 -> (d1 - d2) * 15
        else -> 0
    }

}

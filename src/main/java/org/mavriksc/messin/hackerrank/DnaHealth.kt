package org.mavriksc.messin.hackerrank

import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val scan = Scanner(File("C:\\git\\mystuff\\jus-messin\\src\\main\\resources\\DNA-Input.txt"))

    val n = scan.nextLine().trim().toInt()

    val genes = scan.nextLine().split(" ").toTypedArray()

    val health = scan.nextLine().split(" ").map { it.trim().toInt() }.toTypedArray()

    val s = scan.nextLine().trim().toInt()
    var low = Int.MAX_VALUE
    var high = 0

    for (sItr in 1..s) {
        val firstLastd = scan.nextLine().split(" ")

        val first = firstLastd[0].trim().toInt()

        val last = firstLastd[1].trim().toInt()

        val d = firstLastd[2]
        val score = scoreStrand(d, first, last, genes, health)
        low = min(low, score)
        high = max(high, score)
    }
    print("$low $high")
}
// working but failing all but 2 tests. need to not recalc lps
//https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/
fun scoreStrand(d: String, first: Int, last: Int, genes: Array<String>, health: Array<Int>): Int {
    var score = 0
    for (i in first..last) {
        score += d.kmpMatchScore(genes[i])*health[i]
    }
    return score
}

fun String.kmpMatchScore(pattern: String): Int {
    var count = 0
    val m = pattern.length
    val n = this.length
    val lps = pattern.computeLPS()
    var j = 0
    var i = 0
    while (i < n) {
        if (pattern[j] == this[i]) {
            j++
            i++
        }
        if (j == m) {
            count++
            j = lps[j - 1]
        } else if (i<n && pattern[j]!=this[i]){
            if (j!=0)
                j=lps[j-1]
            else
                i++
        }

    }
    return count
}

fun String.computeLPS(): Array<Int> {
    val m = this.length
    val lps = Array<Int>(m) { 0 }
    var i = 1
    var len = 0
    while (i < m) {
        if (this[i] == this[len]) {
            len++
            lps[i] = len
            i++
        } else {
            if (len != 0) {
                len = lps[len - 1]
            } else {
                lps[i] = len
                i++
            }
        }
    }
    return lps
}

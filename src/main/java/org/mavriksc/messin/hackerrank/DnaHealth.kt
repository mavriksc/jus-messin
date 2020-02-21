package org.mavriksc.messin.hackerrank

import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.max
import kotlin.math.min

// working but failing all but 2 tests. need to not recalc lps
// not recalc lps but still failing same tests timeout
// https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/

// IDEAS:
//https://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/
 

// genes

//make map of genes to index list


fun main(args: Array<String>) {
    val scan = Scanner(File("D:\\code\\jus-messin\\src\\main\\resources\\DNA-2.txt"))

    val n = scan.nextLine().trim().toInt()

    val genes = scan.nextLine().split(" ").toTypedArray()

    val health = scan.nextLine().split(" ").map { it.trim().toInt() }.toTypedArray()

    val s = scan.nextLine().trim().toInt()
    var low = Long.MAX_VALUE
    var high = 0L
    val start = Date()
    for (sItr in 1..s) {
        val firstLastd = scan.nextLine().split(" ")

        val first = firstLastd[0].trim().toInt()

        val last = firstLastd[1].trim().toInt()

        val d = firstLastd[2]
        val score = scoreStrand(d, first, last, genes, health)
        low = min(low, score)
        high = max(high, score)
        counts.clear()
    }
    val end = Date()
    println(end.time-start.time)
    print("$low $high")
}

var lpss: MutableMap<String, Array<Int>> = mutableMapOf()
var counts: MutableMap<String, Long> = mutableMapOf()

fun scoreStrand(d: String, first: Int, last: Int, genes: Array<String>, health: Array<Int>): Long {
    var score = 0L
    for (i in first..last) {
        score += d.kmpMatchScore(genes[i]) * health[i]
    }
    return score
}

fun String.kmpMatchScore(pattern: String): Long {
    return counts.getOrPut(pattern) {
    var count = 0L
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
        } else if (i < n && pattern[j] != this[i]) {
            if (j != 0)
                j = lps[j - 1]
            else
                i++
        }
    }
    return count
    }
}

fun String.computeLPS(): Array<Int> {
    return lpss.getOrPut(this) {
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
}

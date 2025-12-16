package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.random.readFile
import java.util.*

fun main() {
    printQueuePartOne()
    printQueuePartTwo()

}

fun printQueuePartOne() {
    // can we construct an ordering of the pages completely
    val (orderings, pamphlets) = getInput()
    val pagesRight = orderings.groupBy({ it[0] }, { it[1] })
    val pagesLeft = orderings.groupBy({ it[1] }, { it[0] })
    println(pamphlets.filter { filterPamphlets(it, pagesRight, pagesLeft) }.map { it[it.lastIndex / 2] }.sum())

}

fun filterPamphlets(pam: List<Int>, pagesRight: Map<Int, List<Int>>, pagesLeft: Map<Int, List<Int>>): Boolean {
    pam.forEachIndexed { index, key ->
        val leftOf = pam.slice(0 until index)
        val rightOf = pam.slice(index + 1 until pam.size)
        if (!Collections.disjoint(pagesRight.getOrDefault(key, emptyList()), leftOf)
            || !Collections.disjoint(pagesLeft.getOrDefault(key, emptyList()), rightOf))
            return false
    }
    return true
}

fun printQueuePartTwo() {
    val (orderings, pamphlets) = getInput()
    val pagesRight = orderings.groupBy({ it[0] }, { it[1] })
    val pagesLeft = orderings.groupBy({ it[1] }, { it[0] })
    println(pamphlets.filter { !filterPamphlets(it, pagesRight, pagesLeft) }
        .map {
            val fixed = it.sortedWith(kotlin.Comparator { o1, o2 -> if (pagesRight.getOrDefault(o1, emptyList()).contains(o2)) -1 else 1 })
            fixed[fixed.lastIndex / 2] }
        .sum())

}
fun getInput(): Pair<List<List<Int>>, List<List<Int>>> {
    val lines = "advent/24/5/input.txt".readFile()
    val lb = lines.indexOf("")
    val orderings = (0 until lb).map { lines[it].split('|').map(String::toInt) }
    //println(orderings.joinToString("\n"))
    val pamphlets = (lb + 1..lines.lastIndex).map { lines[it].split(',').map(String::toInt) }
    //println(pamphlets.joinToString("\n"))
    return Pair(orderings, pamphlets)
}
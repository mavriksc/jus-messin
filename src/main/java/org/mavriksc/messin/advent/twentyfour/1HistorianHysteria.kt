package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.readFile
import org.mavriksc.messin.toReader
import kotlin.math.abs

// create a read file function that allows you to pass in an operation maybe


fun main() {
    var start = System.currentTimeMillis()
    partOne()
    println("${System.currentTimeMillis() - start} ms")
    //partTwo()
    start = System.currentTimeMillis()
    tryUseLines()
    println("${System.currentTimeMillis() - start} ms")

}

fun tryUseLines() {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    "advent/24/one/input.txt".toReader().forEachLine {
        val parts = it.split(" ")
        left.add(parts[0].toInt())
        right.add(parts[3].toInt())
    }
    right.sort()
    val answer = left.sorted().mapIndexed { index: Int, i: Int -> abs(right[index] - i) }.sum()
    println(answer)

}

fun partTwo() {
    val lines = "advent/24/one/input.txt".readFile()
    val lists = twoSortedLists(lines, false)
    val counts = lists.second.groupingBy { it }.eachCount()
    val answer = lists.first.map { counts.getOrDefault(it, 0) * it }.sum()
    println(answer)
}

fun partOne() {
    val lines = "advent/24/one/input.txt".readFile()
    val lists = twoSortedLists(lines)
    val answer = lists.first.mapIndexed { index: Int, i: Int -> abs(lists.second[index] - i) }.sum()
    println(answer)
}

fun twoSortedLists(lines: List<String>, sorted: Boolean = true): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    lines.forEach {
        val parts = it.split(" ")
        left.add(parts[0].toInt())
        right.add(parts[3].toInt())
    }
    return if (sorted) Pair(left.sorted(), right.sorted())
    else Pair(left, right)
}
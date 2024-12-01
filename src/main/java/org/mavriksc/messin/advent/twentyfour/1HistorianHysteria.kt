package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.mapLines
import org.mavriksc.messin.readFile
import kotlin.math.abs
// create a read file function that allows you to pass in an operation maybe


fun main() {
    //partOne()
    //partTwo()
    tryUseLines()

}

fun tryUseLines() {
    "advent/24/one/test.txt".mapLines { it.split(" ") }
}

fun partTwo() {
    val lines = "advent/24/one/input.txt".readFile()
    val lists = twoSortedLists(lines,false)
    val counts = lists.second.groupingBy { it }.eachCount()
    val answer = lists.first.map { counts.getOrDefault(it, 0) * it }.sum()
    println(answer)
}

fun partOne(){
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
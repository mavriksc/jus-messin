package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.forEachLine
import org.mavriksc.messin.readFile
import org.mavriksc.messin.toReader
import kotlin.math.abs


fun main() {
    // only way to tell perf for sure  is to just run each one independently
    // as vm seems to do some improvements even just reading once is very close to a dumb approach
    var start = System.currentTimeMillis()
    partOne() //42 ms
    println("Part one dirty: ${System.currentTimeMillis() - start} ms")
    start = System.currentTimeMillis()
    tryUseLines()//47 ms
    println("tryUseLines: ${System.currentTimeMillis() - start} ms")
    start = System.currentTimeMillis()
    tryUseLinesDirectly()//47
    println("tryUseLinesDirectly: ${System.currentTimeMillis() - start} ms")
    println("Part Two:")
    start = System.currentTimeMillis()
    partTwo()
    println("partTwo: ${System.currentTimeMillis() - start} ms")
    start = System.currentTimeMillis()
    partTwoImproved()
    println("partTwoImproved: ${System.currentTimeMillis() - start} ms")
    start = System.currentTimeMillis()
    bofum() // 47 ms
    println("bofum: ${System.currentTimeMillis() - start} ms")

}

fun bofum() {
    val (left, right) = pair()
    val rightSorted = right.sorted()
    println( left.sorted().mapIndexed { index: Int, i: Int -> abs(rightSorted[index] - i) }.sum())
    val counts = right.groupingBy { it }.eachCount()
    println(left.map { counts.getOrDefault(it, 0) * it }.sum())

}

fun tryUseLinesDirectly() {
    val (left, right) = "advent/24/1/input.txt".toReader().useLines { lines ->
        lines.map {
            val parts = it.split(" ")
            Pair(parts[0].toInt(), parts[3].toInt())
        }.unzip()
        //^^^ must use terminal op inside useLines scope or buffer will be closed.
    }
    val rightSorted = right.sorted()
    val answer = left.sorted().mapIndexed { index: Int, i: Int -> abs(rightSorted[index] - i) }.sum()
    println(answer)
}

fun tryUseLines() {
    val (left, right) = pair()
    right.sort()
    val answer = left.sorted().mapIndexed { index: Int, i: Int -> abs(right[index] - i) }.sum()
    println(answer)

}

private fun pair(): Pair<MutableList<Int>, MutableList<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    "advent/24/1/input.txt".forEachLine {
        val parts = it.split(" ")
        left.add(parts[0].toInt())
        right.add(parts[3].toInt())
    }
    return Pair(left, right)
}

fun partTwoImproved() {
    val (left, right) = pair()
    val counts = right.groupingBy { it }.eachCount()
    println(left.map { counts.getOrDefault(it, 0) * it }.sum())
}

fun partTwo() {
    val lines = "advent/24/1/input.txt".readFile()
    val lists = twoSortedLists(lines, false)
    val counts = lists.second.groupingBy { it }.eachCount()
    val answer = lists.first.map { counts.getOrDefault(it, 0) * it }.sum()
    println(answer)
}

fun partOne() {
    val lines = "advent/24/1/input.txt".readFile()
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
    return if (sorted) {
        left.sort()
        right.sort()
        Pair(left, right)
    } else Pair(left, right)
}
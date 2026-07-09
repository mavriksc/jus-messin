package org.mavriksc.messin.advent.five

import org.mavriksc.messin.random.readFile

fun main() {

    val input = if (true)
        "advent/five/input6.txt".readFile()
    else
        ("123 328  51 64 \n" +
                " 45 64  387 23 \n" +
                "  6 98  215 314\n" +
                "*   +   *   +  ").split("\n")
    println(fiveDaySixPartOne(input))
    println(fiveDaySixPartTwo(input))
}

fun fiveDaySixPartOne(input: List<String>): Long {
    val numbers = input.dropLast(1)
        .map { line -> line.trim().split(" +".toRegex()).map { it.toLong() } }
        .transposeList()
    val ops = input.last().replace(" ", "")
    return doTheMath(numbers, ops)
}

fun fiveDaySixPartTwo(input: List<String>): Long {
    // from left to right read down in strips until all characters are spaces.
    // this is 1 set
    // each element combines top to bottom in to a number (Long)
    // then ops as normal
    val numbersRows = input.dropLast(1)
    val numbers = (0..numbersRows[0].lastIndex)
        .map { colIndex -> numbersRows.map { row -> row[colIndex] }.joinToString("") }
        .map { if (it.trim().isNotEmpty()) it.trim().toLong() else -1 }
        .fold(mutableListOf(mutableListOf<Long>())) { acc, item ->
            if (item == -1L) {
                acc.add(mutableListOf()) // Start a new sublist when predicate matches
            } else {
                acc.last().add(item)     // Append to the current sublist
            }
            acc
        }
    val ops = input.last().replace(" ", "")
    return doTheMath(numbers, ops)
}

fun doTheMath(numbers: List<List<Long>>, ops: String): Long {
    return numbers.mapIndexed { index, row ->
        when (val op = ops[index]) {
            '+' -> row.sum()
            '*' -> row.reduce { acc, i -> acc * i }
            else -> throw IllegalArgumentException("Invalid operation: $op")
        }
    }.sum()
}

fun <T> List<List<T>>.transposeList(): List<List<T>> {
    if (this.isEmpty()) return emptyList()
    val cols = this[0].size

    return (0 until cols).map { colIndex ->
        this.map { row -> row[colIndex] }
    }
}

package org.mavriksc.messin.advent22


fun String.readFile() = ClassLoader.getSystemResourceAsStream(this)?.bufferedReader()?.readLines()
fun main() {
    //dayOnePuzzleOne()
    //dayOnePuzzleTwo()
    //dayTwoPuzzleOne()
    //dayTwoPuzzleTwo()
    dayThreePuzzleOne()
}

fun dayThreePuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_3_input.txt".readFile()!!
    println( lines.map {
        val comp1 = it.substring(0, it.length / 2).toSet()
        val comp2 = it.substring(it.length / 2).toSet()
        val intersect = comp1.intersect(comp2).first().toInt()
        if (intersect > 90) intersect - 96 else intersect - 38
    }.sum())

}

fun dayTwoPuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_2_input.txt".readFile()
    var points = 0
    lines?.forEach {
        points += when (it.first()) {
            'A' -> when (it.last()) { // ROCK
                'X' -> 3 + 0 // LOSS
                'Y' -> 1 + 3 // DRAW
                'Z' -> 2 + 6 // WIN
                else -> 0
            }

            'B' -> when (it.last()) { // PAPER
                'X' -> 1 + 0 // LOSS
                'Y' -> 2 + 3 // DRAW
                'Z' -> 3 + 6 // WIN
                else -> 0
            }

            'C' -> when (it.last()) { //SCISSORS
                'X' -> 2 + 0 // LOSS
                'Y' -> 3 + 3 // DRAW
                'Z' -> 1 + 6 // WIN
                else -> 0
            }

            else -> 0
        }

    }
    println(points)
}

fun dayTwoPuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_2_input.txt".readFile()
    var points = 0
    lines?.forEach {
        points += pointsForLine(it)
    }
    println(points)

}

fun pointsForLine(line: String): Int {
    // decrypt ?
    when (line.first()) {
        'A' -> when (line.last()) { // ROCK
            'X' -> return 1 + 3 // ROCK
            'Y' -> return 2 + 6 // PAPER
            'Z' -> return 3 + 0 //SCISSORS
        }

        'B' -> when (line.last()) { // PAPER
            'X' -> return 1 + 0 // ROCK
            'Y' -> return 2 + 3 // PAPER
            'Z' -> return 3 + 6 //SCISSORS
        }

        'C' -> when (line.last()) { //SCISSORS
            'X' -> return 1 + 6 // ROCK
            'Y' -> return 2 + 0 // PAPER
            'Z' -> return 3 + 3 //SCISSORS
        }
    }
    return 0
}

fun dayOnePuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_1_input.txt".readFile()
    val cals = mutableListOf<Int>(0)

    lines?.forEach {
        if (it.isEmpty())
            cals.add(0)
        else {
            // add to last element in cals
            cals[cals.lastIndex] = cals.last() + it.toInt()
        }
    }
    println(cals.max())
}

fun dayOnePuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_1_input.txt".readFile()
    val top = mutableListOf(0, 0, 0)
    var curSum = 0
    lines?.forEach {
        if (it.isEmpty()) {
            pushToList(curSum, top)
            curSum = 0
        } else {
            curSum += it.toInt()
        }
    }
    println(top.slice((0..2)).sum())
}

fun pushToList(curSum: Int, top: MutableList<Int>) {
    run breaking@{
        (0..2).forEach {
            if (curSum > top[it]) {
                top.add(it, curSum)
                return@breaking
            }
        }
    }
}

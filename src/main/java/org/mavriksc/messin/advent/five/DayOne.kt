package org.mavriksc.messin.advent.five

import org.mavriksc.messin.random.readFile

fun main() {
    partTwo()
}


fun partOne() {
    var dial = 50
    var pw = 0
    val input = "advent/five/input1.txt".readFile()
    input.forEach {
        when {
            it.startsWith("L") -> dial -= it.drop(1).toInt()
            it.startsWith("R") -> dial += it.drop(1).toInt()
        }
        dial = (dial + 100) % 100
        if (dial == 0) pw++
        println("dial:$dial\tpw:$pw")
    }
}

fun partTwo() {
    var dial = 50
    var pw = 0
    val input = "advent/five/input1.txt".readFile()
    input.forEach {
        val newState = partTwoMethod(it, dial, pw)
        dial = newState.first
        pw = newState.second
    }
    println(pw)
}

private fun partTwoMethod(string: String, dial: Int, pw: Int): Pair<Int, Int> {
    val turn = string.drop(1).toInt()
    val turnLeft = if (string.startsWith("L")) -1 else 1
    val fullTurns = turn / 100
    val remainder = turn % 100
    val zeroPassesAndStops = countZeroPassesAndStops(dial, remainder, turnLeft)
    val dial1 = (dial + turn * turnLeft).mod(100)
    val pw1 = pw + fullTurns + zeroPassesAndStops

    println("turn:${turn * turnLeft}\tfull turns:$fullTurns\tremainder:$remainder\tdial:$dial1\tpw:$pw1")
    return Pair(dial1, pw1)
}

private fun countZeroPassesAndStops(dial: Int, turn: Int, turnLeft: Int): Int {
    if (turn == 0) return 0

    val distanceToZero = when (turnLeft) {
        -1 -> if (dial == 0) 100 else dial
        else -> if (dial == 0) 100 else 100 - dial
    }

    return if (turn >= distanceToZero) 1 else 0
}

fun partTwoFullDumb() {
    var dial = 50
    var pw = 0
    val input = "advent/five/input1.txt".readFile()
    input.forEach {
        val newState = fullDumbMethod(it, dial, pw)
        dial = newState.first
        pw = newState.second
    }
    println(pw)

}

private fun fullDumbMethod(string: String, dial: Int, pw: Int): Pair<Int, Int> {
    var dial1 = dial
    var pw1 = pw
    val turn = string.drop(1).toInt()
    val turnLeft = if (string.startsWith("L")) -1 else 1
    val fullTurns = turn / 100
    val remainder = turn % 100
    val newPosAndPasses = turnDialCountZeroPassesAndStops(dial1, remainder, turnLeft)
    pw1 += newPosAndPasses.first + fullTurns
    dial1 = newPosAndPasses.second
    return Pair(dial1, pw1)
}


fun turnDialCountZeroPassesAndStops(init: Int, turn: Int, turnLeft: Int): Pair<Int, Int> {
    var t = turn
    var position = init
    var answer = 0
    while (t > 0) {
        t--
        position += turnLeft
        if (position == 0 || position == 100) answer++
        if (position == -1) position = 99
        if (position == 100) position = 0
    }
    return Pair(answer, position)
}

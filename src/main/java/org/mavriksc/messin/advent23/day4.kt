package org.mavriksc.messin.advent23

import org.mavriksc.messin.readFile
import kotlin.math.pow

fun main() {
    val lines = "advent23/4.txt".readFile()!!
    fourPart1(lines)
    fourPart2(lines)
}

fun fourPart1(lines: List<String>) {
    println(lines.map { line ->
        val matchingNumbers = howManyMatch(line)
        val score = if (matchingNumbers == 0) 0 else (2.0.pow(matchingNumbers - 1)).toInt()
        score
    }.sum())
}

fun fourPart2(lines: List<String>) {
    val bonusCards = IntArray(lines.size) { 1 }
    println(lines.mapIndexed { index, line ->
        val matchingNumbers = howManyMatch(line)
        for (x in index + 1..index + matchingNumbers) {
            bonusCards[x] += bonusCards[index]
        }
        bonusCards[index]
    }.sum())
}

private fun howManyMatch(line: String): Int {
    val numbers = line.split(':')[1].split('|')
    val winningNumbers = numbers[0].split(' ').filter { it.isNotEmpty() }.toSet()
    val ourNumbers = numbers[1].split(' ').filter { it.isNotEmpty() }.toSet()
    val matchingNumbers = ourNumbers.intersect(winningNumbers).size
    return matchingNumbers
}

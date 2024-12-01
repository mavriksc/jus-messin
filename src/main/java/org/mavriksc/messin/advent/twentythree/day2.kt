package org.mavriksc.messin.advent.twentythree

import org.mavriksc.messin.readFile
import kotlin.math.max

fun main() {
    val lines = "advent/23/2ainput.txt".readFile()!!
    val r = 12
    val g = 13
    val b = 14
    var gameNum = 0
    println("part 1" + lines.map {
        gameNum++
        if (roundOnePossible(it, r, g, b)) gameNum else 0
    }.sum())
    println("part 2 " + lines.map {
        mapCubePower(it)
    }.sum())
}

fun mapCubePower(line: String): Int {
    return line.split(':')[1]
        .split(';')
        .map { round ->
            round.split(',')
                .map { colorCountString ->
                    colorCountString
                        .trim()
                        .split(' ')
                }
        }.flatten()
        .fold(mutableMapOf<String, Int>()) { m, value ->
            m[value[1]] = max(m.getOrDefault(value[1], 0), value[0].toInt())
            m
        }.values
        .fold(1) { prod, n -> prod * n }
}

fun roundOnePossible(line: String, r: Int, g: Int, b: Int): Boolean {
    val rounds = line.split(':')[1].split(';')
    rounds.map { round ->
        round.split(',').map { colorCountString ->
            val colorCount = colorCountString.trim().split(' ')
            when (colorCount[1].first()) {
                'r' -> if (colorCount[0].toInt() > r) return false
                'b' -> if (colorCount[0].toInt() > b) return false
                'g' -> if (colorCount[0].toInt() > g) return false
            }
        }
    }
    return true
}



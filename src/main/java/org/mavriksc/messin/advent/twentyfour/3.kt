package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.readFile

val pattern1 = "mul\\((\\d+),(\\d+)\\)".toRegex()
val pattern2 = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)".toRegex()

fun main() {
    day3p1()
    day3p2()
}

fun day3p1() {
    val lines = "advent/24/three/input.txt".readFile()
    println(lines.map { line ->
        pattern1.findAll(line)
            .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }.sum()
    }.sum())
}

fun day3p2() {
    val lines = "advent/24/three/input.txt".readFile()
    var mulEnabled = true
    println(lines.map { line ->
        pattern2.findAll(line)
            .map {
                var result: Int? = null
                when (it.groupValues[0]) {
                    "do()" -> mulEnabled = true
                    "don't()" -> mulEnabled = false
                    else -> result = if (mulEnabled)
                                        it.groupValues[1].toInt() * it.groupValues[2].toInt()
                                     else 0
                }
                result
            }.filterNotNull().sum()
    }.sum())
}
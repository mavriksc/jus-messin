package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.readFile

val pattern1 = "mul\\((\\d+),(\\d+)\\)".toRegex()

fun main() {
    day3p1()
}

fun day3p1() {
    val lines = "advent/24/three/input.txt".readFile()
    println(lines.map { line ->
        pattern1.findAll(line)
            .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }.sum()
    }.sum())
}

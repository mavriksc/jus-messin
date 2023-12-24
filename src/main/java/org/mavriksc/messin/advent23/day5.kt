package org.mavriksc.messin.advent23

import org.mavriksc.messin.readFile

fun main() {
    val lines = "advent23/5s.txt".readFile()!!
    fivePart1(lines)
}

fun fivePart1(lines: List<String>) {
    // parse input into list of seeds and the maps
    // fully mapped to location and min location  is answer
    val seeds = listOf<Int>()
    seeds.map { seed -> seed }.map { soil -> soil }.min()
}

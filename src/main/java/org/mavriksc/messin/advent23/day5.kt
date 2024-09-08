package org.mavriksc.messin.advent23

import org.mavriksc.messin.readFile

fun main() {
    val lines = "advent23/5i.txt".readFile()!!
    //fivePart1(lines)
    fivePart2(lines)
}

fun fivePart2(lines: List<String>) {
    // just generating all the seeds times out.
    // will do this by mapping the ranges by endpoints
    // sort the seeds and the maps  and walk through the 2 lists
    // will need to sort ranges after every step
    val (seeds, maps) = parseInput(lines)
    var ranges = (0..seeds.lastIndex step 2).map {
        (seeds[it] until seeds[it] + seeds[it + 1])
    }.sortedBy { it.first }
    maps.forEach {
        ranges = mapRanges(ranges,it)
    }
    println(ranges[0].first)
}

fun mapRanges(ranges: List<LongRange>, maps: List<List<Long>>): List<LongRange> {
    val outRange = mutableListOf<LongRange>()
    val mapRanges = maps.sortedBy { it[1] }
    var mapsIndex = 0
    ranges.forEach {
        //does the current map, map part of this range?
        // inc map index until it does
        while (mapsIndex < mapRanges.lastIndex && mapRanges[mapsIndex][1] < it.first() || mapRanges[mapsIndex][1] + mapRanges[mapsIndex][2] < it.first() ) mapsIndex++
        println(it)
        println(mapRanges[mapsIndex])
    }
    return outRange.sortedBy { it.first() }
}

private fun mapToLocation(seeds: List<Long>, maps: List<List<List<Long>>>): Long {
    val minLocation = seeds.asSequence().map { seed -> mapResource(seed, maps[0]) }
        .map { soil -> mapResource(soil, maps[1]) }
        .map { fertilizer -> mapResource(fertilizer, maps[2]) }
        .map { water -> mapResource(water, maps[3]) }
        .map { light -> mapResource(light, maps[4]) }
        .map { temperature -> mapResource(temperature, maps[5]) }
        .map { humidity -> mapResource(humidity, maps[6]) }.toList()
        .min()
    return minLocation!!
}

fun fivePart1(lines: List<String>) {
    // parse input into list of seeds and the maps
    // fully mapped to location and min location  is answer
    val (seeds, maps) = parseInput(lines)

    val minLocation = mapToLocation(seeds, maps)
    println(minLocation)
}

private fun parseInput(lines: List<String>): Pair<List<Long>, MutableList<List<List<Long>>>> {
    val seeds = lines[0].split(':')[1].trim().split(' ').map { it.toLong() }
    val maps = mutableListOf<List<List<Long>>>()
    var line = 3
    do {
        maps.add(getMaps(line, lines))
        line += maps.last().size + 2
    } while (line <= lines.lastIndex)
    return Pair(seeds, maps)
}



fun mapResource(value: Long, maps: List<List<Long>>): Long {
    val activeMap = maps.firstOrNull() { value in it[1] until it[1] + it[2] }
    return if (!activeMap.isNullOrEmpty())
        activeMap[0] + (value - activeMap[1])
    else
        value
}

fun getMaps(startLine: Int, lines: List<String>): List<List<Long>> {
    var line = startLine
    val ret = mutableListOf<List<Long>>()
    while (line <= lines.lastIndex && lines[line].isNotEmpty()) {
        ret.add(lines[line].split(' ').map { it.toLong() })
        line++
    }
    return ret
}

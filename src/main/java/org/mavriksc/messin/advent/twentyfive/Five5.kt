package org.mavriksc.messin.advent.twentyfive

import org.mavriksc.messin.random.readFile

fun main() {
    val sampleOrInput = "sample"
    val lines = "advent/five/${sampleOrInput}5.txt".readFile()
    fiveDayFivePartOne(lines)
    fiveDayFivePartTwo(lines)
}


fun fiveDayFivePartOne(lines: List<String>) {
    val ranges = lines
        .filter { it.contains("-") }
        .map { line ->
            val range = line.split("-").map { it.toLong() }
            (range[0]..range[1])
        }
    val numbers = lines.filter { !it.contains("-") && it.isNotBlank() }.map { it.toLong() }
    println(numbers.count { number -> ranges.any { range -> range.contains(number) } })
}


fun fiveDayFivePartTwo(lines: List<String>) {
    val ranges = lines
        .filter { it.contains("-") }
        .map { line ->
            val range = line.split("-").map { it.toLong() }
            (range[0]..range[1])
        }.mergeOverlapping()
    println(ranges.sumOf { it.endInclusive - it.start + 1 })
}

fun <T : Comparable<T>> List<ClosedRange<T>>.mergeOverlapping(): List<ClosedRange<T>> {
    if (this.isEmpty()) return emptyList()

    // 1. Sort ranges by their starting point
    val sortedRanges = this.sortedWith(compareBy { it.start })
    val merged = mutableListOf<ClosedRange<T>>()

    // 2. Initialize with the first range
    var current = sortedRanges.first()

    for (other in sortedRanges.drop(1)) {
        // 3. If next range starts before or exactly when current ends, they overlap
        if (other.start <= current.endInclusive) {
            // Merge by extending the end point to the maximum of both
            current = current.start..maxOf(current.endInclusive, other.endInclusive)
        } else {
            // No overlap, commit current range and move to next
            merged.add(current)
            current = other
        }
    }

    // 4. Don't forget to add the final evaluated range
    merged.add(current)
    return merged
}

package org.mavriksc.messin.advent.five

import org.mavriksc.messin.random.readFile

fun main() {
//    val input = ("987654321111111\n" +
//            "811111111111119\n" +
//            "234234234234278\n" +
//            "818181911112111").split("\n")
    val input = "advent/five/input3.txt".readFile()
    // println(partOne(input))
    println(partTwoGreedyWindow(input))


}

fun partOne(input: List<String>): Int {
    return input.sumOf { line ->
        var leftDigitIndex = 0
        (0..<line.lastIndex).forEach { if (line[it] > line[leftDigitIndex]) leftDigitIndex = it }
        var rightDigitIndex = leftDigitIndex + 1
        (leftDigitIndex + 1..line.lastIndex).forEach { if (line[it] > line[rightDigitIndex]) rightDigitIndex = it }
        "${line[leftDigitIndex]}${line[rightDigitIndex]}".toInt()
    }
}

// map of numbers to list of index
// from 9 to 1

fun partTwo(input: List<String>): Long {
    return input.sumOf { line ->
        val num = lineToJoltagePartTwoForwardPass(line)
        println(num)
        num
    }
}

fun lineToJoltage(line: String): Long {
    val map = line.mapIndexed { index, ch -> Pair(ch, index) }
        .groupByTo(mutableMapOf(), { it.first }, { it.second })
        .toMap()
    val keys = map.keys.sorted().reversed().iterator()
    var curKey = keys.next()
    val indexes = mutableListOf<Int>()
    while (indexes.size < 12) {
        val need = 12 - indexes.size
        indexes.addAll(
            map[curKey]!!
                //.filter {  }
                .take(need)
        )
        if (keys.hasNext()) curKey = keys.next()
    }
    val answer = indexes.sorted().map { line[it] }.joinToString("").toLong()
    return answer
}
//terrible algo incoming
//high to low each level  create a new list of possibles by adding each. sort that. add indexes from high to low.


fun doTerribleThings(line: String): Long {
    val map = line.mapIndexed { index, ch -> Pair(ch, index) }
        .groupByTo(mutableMapOf(), { it.first }, { it.second })
        .toMap()
    val keys = map.keys.sorted().reversed().iterator()
    var curKey = keys.next()
    val indexes = mutableListOf<Int>()
    while (indexes.size < 12) {
        val need = 12 - indexes.size

        indexes.addAll(
            map[curKey]!!
                //.filter {  }
                .take(need)
        )
        if (keys.hasNext()) curKey = keys.next()
    }
    val answer = indexes.sorted().map { line[it] }.joinToString("").toLong()
    return answer
}

fun lineToJoltagePartTwoForwardPass(line: String): Long {
    val map = line.mapIndexed { index, ch -> Pair(ch, index) }
        .groupByTo(mutableMapOf(), { it.first }, { it.second })
        .toMap()
    val indexes = mutableListOf<Int>()
    val unaddedIndexes = line.indices.toMutableList()
    val keys = map.keys.sorted().reversed().toMutableList()
    var leftMostAdded = -1

    // Keep taking the highest key that can be added without making it
    // impossible to reach 12 total digits in the original order.
    while (indexes.size < 12) {
        val openSlots = 12 - indexes.size
        val rightMostIndexThatCanBeAdded = line.length - openSlots
        // Pick the largest remaining digit that has at least one valid index
        // in the current window.
        val nextKey = keys.firstOrNull { key ->
            map[key]!!.any { it in unaddedIndexes && it in (leftMostAdded + 1)..rightMostIndexThatCanBeAdded }
        } ?: error("Could not build a 12 digit number from the given input.")
        // Use the left most valid index for that digit so later picks still
        // have as much room as possible.
        val nextIndex = map[nextKey]!!.first {
            it in unaddedIndexes && it in (leftMostAdded + 1)..rightMostIndexThatCanBeAdded
        }

        indexes.add(nextIndex)
        unaddedIndexes.remove(nextIndex)
        leftMostAdded = nextIndex

        // Once every occurrence of a key is in the answer, it can be skipped
        // by later scans.
        keys.removeAll { key -> map[key]!!.all { it in indexes } }
    }

    return indexes.sorted().map { line[it] }.joinToString("").toLong()
}

fun partTwoGreedyWindow(input: List<String>): Long {
    return input.sumOf { line ->
        lineToTwelveDigitJoltageGreedyWindow(line)
    }
}

fun lineToTwelveDigitJoltageGreedyWindow(line: String): Long {
    val answer = StringBuilder()
    var nextSearchStart = 0

    while (answer.length < 12) {
        val digitsLeftToPick = 12 - answer.length
        val lastIndexAllowed = line.length - digitsLeftToPick
        var bestIndex = nextSearchStart

        // Search only the indexes that still leave enough digits to complete
        // a 12 digit answer, then keep the largest digit in that window.
        (nextSearchStart..lastIndexAllowed).forEach { index ->
            if (line[index] > line[bestIndex]) {
                bestIndex = index
            }
        }

        answer.append(line[bestIndex])
        nextSearchStart = bestIndex + 1
    }

    return answer.toString().toLong()
}

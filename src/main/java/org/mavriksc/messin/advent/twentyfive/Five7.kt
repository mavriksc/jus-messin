package org.mavriksc.messin.advent.twentyfive

import org.mavriksc.messin.random.readFile
fun main() {
    val sampleOrInput = "sample"
    val input = "advent/five/${sampleOrInput}7.txt".readFile()
    fiveDaySevenPartOne(input)
    fiveDaySevenPartTwo(input)
}

fun fiveDaySevenPartOne(input: List<String>) {
    val beamFrontIndexes = input[0].mapIndexed { index, c ->
        if (c == 'S') index
        else null
    }.filterNotNull().toMutableSet()

    val splitters = input.drop(2).filterIndexed { index, _ -> index % 2 == 0 }
        .map {
            it.mapIndexed { index, c ->
                if (c == '^') index
                else null
            }.filterNotNull().toSet()
        }
    //splitters for each remove index of splitter from index of wavefront.
    // add index below and above if in range
    val answer = splitters.sumOf { splitter ->
        val splits = beamFrontIndexes.intersect(splitter).count()
        beamFrontIndexes.removeAll(splitter)
        beamFrontIndexes.addAll(splitter.map { it + 1 }.filter { it < input[0].length })
        beamFrontIndexes.addAll(splitter.map { it - 1 }.filter { it >= 0 })
        splits
    }

    println(answer)
}

fun fiveDaySevenPartTwo(input: List<String>) {
    val height = input.size / 2 - 1
    val width = input[0].length
    val dpArray = List(height) { MutableList(width) { 0L } }
    val splitters = input.drop(2).filterIndexed { index, _ -> index % 2 == 0 }

    splitters.asReversed().forEachIndexed { rowIndex, row ->
        val mappedRowIndex = splitters.lastIndex - rowIndex
        row.forEachIndexed { colIndex, char ->
            // dpArray[rowNum][colNum] = if(char=='^') 1+ `next row left of and right of if exists` else next row same col if exists
            //this will go out of bounds  only include sums where col num is valid
            if (char == '^') {
                when {
                    mappedRowIndex == splitters.lastIndex -> dpArray[mappedRowIndex][colIndex] = 1
                    colIndex > 0 && colIndex < row.lastIndex -> dpArray[mappedRowIndex][colIndex] = 1 +
                            dpArray[mappedRowIndex + 1][colIndex - 1] +
                            dpArray[mappedRowIndex + 1][colIndex + 1]

                    colIndex == 0 -> dpArray[mappedRowIndex][colIndex] = 1 + dpArray[mappedRowIndex + 1][1]
                    colIndex == row.lastIndex -> dpArray[mappedRowIndex][colIndex] = 1 +
                            dpArray[mappedRowIndex + 1][colIndex - 1]
                }
            } else {
                when {
                    mappedRowIndex == splitters.lastIndex -> {}
                    else -> dpArray[mappedRowIndex][colIndex] = dpArray[mappedRowIndex + 1][colIndex]
                }
            }

        }
    }
    splitters.forEach { println(it) }
    dpArray.forEach { println(it) }
    // +1 for the original timeline
    println(dpArray[0][input[0].indexOfFirst { it == 'S' }] + 1)
}

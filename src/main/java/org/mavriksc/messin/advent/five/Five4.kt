package org.mavriksc.messin.advent.five

import org.mavriksc.messin.random.readFile

fun main() {
//    val input = ("..@@.@@@@.\n" +
//            "@@@.@.@.@@\n" +
//            "@@@@@.@.@@\n" +
//            "@.@@@@..@.\n" +
//            "@@.@@@@.@@\n" +
//            ".@@@@@@@.@\n" +
//            ".@.@.@.@@@\n" +
//            "@.@@@.@@@@\n" +
//            ".@@@@@@@@.\n" +
//            "@.@.@@@.@.").split("\n")
    val input = "advent/five/input4.txt".readFile()
    println(dfPartOne(input))
    println(dfPartTwo(input))
}

private fun dfPartOne(input: List<String>): Int {
    val width = input[0].lastIndex
    val height = input.lastIndex
    return input.mapIndexed { row, string ->
        string.mapIndexed { col, c ->
            c == '@' &&
                    dirs(col, row, height, width)
                        .count { input[it.second + row][it.first + col] == '@' } < 4
        }.count { it }
    }.sum()
}


private fun dirs(x: Int, y: Int, height: Int, width: Int): List<Pair<Int, Int>> {
    val eightDirs =
        listOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1), Pair(-1, 0), Pair(0, 1), Pair(-1, 1), Pair(1, 0), Pair(1, 1))
            .toMutableList()
    when {
        x == 0 && y == 0 -> eightDirs.removeIf { it.first < 0 || it.second < 0 }
        x == width && y == height -> eightDirs.removeIf { it.first > 0 || it.second > 0 }
        x == 0 && y == height -> eightDirs.removeIf { it.first < 0 || it.second > 0 }
        x == width && y == 0 -> eightDirs.removeIf { it.first > 0 || it.second < 0 }
        x == 0 -> eightDirs.removeIf { it.first < 0 }
        y == 0 -> eightDirs.removeIf { it.second < 0 }
        x == width -> eightDirs.removeIf { it.first > 0 }
        y == height -> eightDirs.removeIf { it.second > 0 }
    }
    return eightDirs.toList()
}

private fun dfPartTwo(input: List<String>): Int {
    val newState = input.map { it.map { ch -> if (ch == '@') 1 else 0 }.toMutableList() }
    val startCount = newState.sumOf { it.sum() }
    val width = input[0].lastIndex
    val height = input.lastIndex
    do {
        val locationsToRemove = newState.flatMapIndexed { row, string ->
            string.mapIndexed { col, c ->
                if (c == 1 &&
                    dirs(col, row, height, width)
                        .count { newState[it.second + row][it.first + col] == 1 } < 4
                )
                    Pair(col, row)
                else
                    null
            }.filterNotNull()
        }
        locationsToRemove.forEach { (x, y) ->
            newState[y][x] = 0
        }
        println(locationsToRemove.size)
    } while (locationsToRemove.isNotEmpty())

    return startCount - newState.sumOf { it.sum() }
}
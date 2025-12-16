package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.random.readFile

fun main() {
    guardGallivantPartOne()
}

fun guardGallivantPartOne() {
    val indicator = ">v<^"
    val dirs = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
    val lines = "advent/24/6/input.txt".readFile()
    val visited = mutableSetOf<Pair<Int, Int>>()
    var (row, col) = lines
        .filter { it.contains("[$indicator]".toRegex()) }
        .map { line ->
            val row = lines.indexOf(line)
            val col = line.indexOf(line.first { it != '#' && it != '.' })
            Pair(row, col)
        }
        .first()
    var dir = indicator.indexOf(lines[row][col])
    println(dir)

    do {
        //add current position to list
        visited.add(row to col)
        // can we continue in same direction
        // if not find next dir
        while (lines.indices.contains(row + dirs[dir].first) &&
            lines[row].indices.contains(col + dirs[dir].second) &&
            lines[row + dirs[dir].first][col + dirs[dir].second] == '#') {
            dir = (dir + 1) % 4
        }
        // take step
        row += dirs[dir].first
        col += dirs[dir].second

    } while (row >= 0 && row <= lines.lastIndex && col >= 0 && col <= lines[0].lastIndex)
    println(visited.size)

}

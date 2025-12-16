package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.random.readFile

fun main() {
    ceresSearchPartOne()
    ceresSearchPartTwo()
}

private fun ceresSearchPartOne() {
    val input = "advent/24/4/input.txt".readFile()
    val ans = input.mapIndexed { row, s ->
        s.mapIndexed { col, c -> if (c == 'X') getStrings(input, row, col).count { it == "XMAS" } else 0 }.sum()
    }.sum()
    println(ans)
}

fun getStrings(input: List<String>, row: Int, col: Int): List<String> {
    val dirs =
        listOf(Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(0, -1), Pair(-1, -1), Pair(-1, 0), Pair(1, -1), Pair(-1, 1))
    val wordLength = 4
    val ans = dirs.map {
        var temp = ""
        var r = row
        var c = col
        var chars = 0
        while (r >= 0 && r <= input.lastIndex && c >= 0 && c <= input[0].lastIndex && chars < wordLength) {
            temp += input[r][c]
            r += it.first
            c += it.second
            chars++
        }
        temp
    }
    return ans
}

fun ceresSearchPartTwo() {
    val input = "advent/24/4/input.txt".readFile()
    val ans = input.mapIndexed { row, s ->
        s.mapIndexed { col, c -> if (c == 'A' && isXmas(input, row, col)) 1 else 0 }.sum()
    }.sum()
    println(ans)
}

fun isXmas(input: List<String>, row: Int, col: Int): Boolean {
    if (row==0 || row == input.lastIndex) return false
    if (col==0 || col == input[0].lastIndex) return false
    val cross = "${input[row-1][col-1]}${input[row-1][col+1]}${input[row+1][col+1]}${input[row+1][col-1]}"
    return cross =="MMSS" ||cross =="SMMS" ||cross =="MSSM" ||cross =="SSMM"

}

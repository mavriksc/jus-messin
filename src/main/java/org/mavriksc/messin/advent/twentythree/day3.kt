package org.mavriksc.messin.advent.twentythree

import org.mavriksc.messin.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = "advent/23/3input.txt".readFile()
    part1(lines)
    part2(lines)
}

fun part2(lines: List<String>) {
    // find the '*' if the adjacent part number count is 2  parse and multiply else return 0. sum results
    var total = 0
    lines.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { colIndex, c ->
            if (c == '*') {
                total += findAroundNums(rowIndex, colIndex, lines)
            }
        }
    }
    println(total)
}

fun findAroundNums(row: Int, col: Int, lines: List<String>): Int {
    val boundStart = max(col - 1, 0)
    val boundEnd = min(col + 1, lines[row].lastIndex)
    val lookAbove = row > 0
    val lookBelow = row < lines.lastIndex
    val nums = mutableListOf<Int>()
    // if the above/below middle is a digit it can only have 1 number expand from that point and include that number
    // else "check" the corners and expand from those points
    // a * can touch 0->2 numbers above on row and below  on row it's just the count of digits touching
    // above and below. ...->0; d..,.d.,..d,dd.,.dd,ddd->1; d.d->2
    if (lines[row][boundStart].isDigit()) nums.add(numFromPoint(boundStart, lines[row]))
    if (lines[row][boundEnd].isDigit()) nums.add(numFromPoint(boundEnd, lines[row]))
    if (lookAbove && lines[row - 1][col].isDigit()) nums.add(numFromPoint(col, lines[row - 1]))
    else {//check corners
        if (col - 1 > 0 && lines[row - 1][col - 1].isDigit())
            nums.add(numFromPoint(col - 1, lines[row - 1]))
        if (col + 1 <= lines[row - 1].lastIndex && lines[row - 1][col + 1].isDigit())
            nums.add(numFromPoint(col + 1, lines[row - 1]))
    }
    if (lookBelow && lines[row + 1][col].isDigit()) nums.add(numFromPoint(col, lines[row + 1]))
    else {//check corners
        if (col - 1 > 0 && lines[row + 1][col - 1].isDigit())
            nums.add(numFromPoint(col - 1, lines[row + 1]))
        if (col + 1 <= lines[row + 1].lastIndex && lines[row + 1][col + 1].isDigit())
            nums.add(numFromPoint(col + 1, lines[row + 1]))
    }
    return if (nums.size == 2) nums[0] * nums[1] else 0
}

fun numFromPoint(col: Int, line: String): Int {
    val leftString = if (col - 1 > 0 && line[col - 1].isDigit()) lookLeft(col - 1, line) else ""
    val rightString = if (col + 1 <= line.lastIndex && line[col + 1].isDigit()) lookRight(col + 1, line) else ""
    val num = (leftString + line[col] + rightString).toInt()
    //println(num)
    return num
}

fun lookRight(col: Int, line: String): String {
    var end = col
    while (end + 1 <= line.lastIndex && line[end + 1].isDigit()) end++
    return line.slice(col..end)

}

fun lookLeft(col: Int, line: String): String {
    var start = col
    while (start - 1 >= 0 && line[start - 1].isDigit()) start--
    return line.slice(start..col)
}

private fun part1(lines: List<String>) {
    var total = 0
    lines.forEachIndexed { rowIndex, line ->
        var currentNum = ""
        line.forEachIndexed { colIndex, c ->
            if (c.isDigit()) currentNum += c
            else if (currentNum.isNotEmpty()) {
                total += if (touchingSymbol(rowIndex, colIndex - currentNum.length, colIndex - 1, lines))
                    currentNum.toInt()
                else 0
                currentNum = ""
            }
        }
        if (currentNum.isNotEmpty()) {
            total += if (touchingSymbol(rowIndex, line.lastIndex - (currentNum.length - 1), line.lastIndex, lines))
                currentNum.toInt()
            else 0
            currentNum = ""
        }
    }
    println(total)
}

fun touchingSymbol(row: Int, colStart: Int, colEnd: Int, lines: List<String>): Boolean {
    val boundStart = max(colStart - 1, 0)
    val boundEnd = min(colEnd + 1, lines[row].lastIndex)
    val lookAbove = row > 0
    val lookBelow = row < lines.lastIndex
    // check the possible ends
    // if not first row check row above
    // if not last row check below
    if (lines[row][boundStart].isSymbol() || lines[row][boundEnd].isSymbol()) return true
    else {
        for (i: Int in boundStart..boundEnd) {
            if (lookAbove && lines[row - 1][i].isSymbol()) return true
            if (lookBelow && lines[row + 1][i].isSymbol()) return true
        }
    }
    return false
}

fun Char.isDigit(): Boolean = this in '0'..'9'

fun Char.isSymbol(): Boolean = !(this == '.' || this in '0'..'9')

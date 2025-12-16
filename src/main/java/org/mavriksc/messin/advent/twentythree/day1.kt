package org.mavriksc.messin.advent.twentythree

import org.mavriksc.messin.random.readFile

fun main() {
    //dayOneA()
    dayOneB()
}
//test
fun dayOneB() {
    val lines = "advent/23/input1-a.txt".readFile()
    var total = 0
    lines.forEach {
        var state = 0
        val mappedRow = it.map { c ->
            val next = transitionFunction(state, c)
            state = next.first
            next.second
        }.filterNotNull().joinToString("")
        val rowVal = (mappedRow.first().toInt() - 48) * 10 + mappedRow.last().toInt() - 48
        //println(rowVal)
        total += rowVal
    }
    println(total)
}

// try to make a FSM  to parse  the input for the numbers
// there are 24 states needed
//  two ideas. as it is sparse on the language a full array is too much
//  1. States = Array[24]<Map<char,<pair<int,int?>
//    if the current state's map has the current char mapped go to that state else state 0
//    if the pair second element is not null it is answer emitted
//  2. transition function  takes int for state and char for input returns a pair<Int,Int?>like above
//   uses when do the 1-9 digits all emitting themselves and then going to state 0
//   then when (state) { 0-24 : when (input)-> 'somechar that makes progres' -> return pair (next state, parsed number or null)}

/// Almost got this right. need to make sure i have any word start letters mapped from intermediate states.
/// there was a bug in seven (eightseven) -> 8 . and also twothree
// fixed by pasting all the initial state edges from other states
// issue was that it would eat the start of a new number while bailing out of the old instead of going right into new
private fun transitionFunction(state: Int, input: Char): Pair<Int, Int?> {
    return if (input.toInt() in 48..57) {
        Pair(0, input.toInt() - 48)
    } else {
        when (state) {
            0 -> {
                when (input) {
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            1 -> {// O in one
                when (input) {
                    'n' -> Pair(2, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    else -> Pair(0, null)
                }
            }

            2 -> {// ON in one
                when (input) {
                    'e' -> Pair(3, 1)
                    'i' -> Pair(23, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            3 -> {// E
                when (input) {
                    'i' -> Pair(9, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            4 -> {// T
                when (input) {
                    'w' -> Pair(5, null)
                    'h' -> Pair(6, null)
                    's' -> Pair(15, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            5 -> {// TW
                when (input) {
                    'o' -> Pair(1, 2)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            6 -> {// TH
                when (input) {
                    'r' -> Pair(7, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            7 -> {// THR
                when (input) {
                    'e' -> Pair(8, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            8 -> {// THRE
                when (input) {
                    'e' -> Pair(3, 3)
                    'i' -> Pair(9, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            9 -> {// EI
                when (input) {
                    'g' -> Pair(20, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            10 -> {// F
                when (input) {
                    'o' -> Pair(11, null)
                    'i' -> Pair(13, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            11 -> {// FO
                when (input) {
                    'u' -> Pair(12, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            12 -> {// FOU
                when (input) {
                    'r' -> Pair(0, 4)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            13 -> {// FI
                when (input) {
                    'v' -> Pair(14, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            14 -> {// FIV
                when (input) {
                    'e' -> Pair(3, 5)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            15 -> {// S
                when (input) {
                    'i' -> Pair(16, null)
                    'e' -> Pair(17, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            16 -> {// SI
                when (input) {
                    'x' -> Pair(0, 6)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            17 -> {// SE
                when (input) {
                    'v' -> Pair(18, null)
                    'i' -> Pair(9, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            18 -> {// SEV
                when (input) {
                    'e' -> Pair(19, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            19 -> {// SEVE
                when (input) {
                    'n' -> Pair(22, 7)
                    'i' -> Pair(9, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    else -> Pair(0, null)
                }
            }

            20 -> {// EIG
                when (input) {
                    'h' -> Pair(21, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            21 -> {// EIGH
                when (input) {
                    't' -> Pair(4, 8)
                    'o' -> Pair(1, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            22 -> {// N
                when (input) {
                    'i' -> Pair(23, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }

            23 -> {// NI
                when (input) {
                    'n' -> Pair(24, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'e' -> Pair(3, null)
                    else -> Pair(0, null)
                }
            }

            24 -> {// NIN
                when (input) {
                    'e' -> Pair(0, 9)
                    'i' -> Pair(23, null)
                    'o' -> Pair(1, null)
                    't' -> Pair(4, null)
                    'f' -> Pair(10, null)
                    's' -> Pair(15, null)
                    'n' -> Pair(22, null)
                    else -> Pair(0, null)
                }
            }
            else -> Pair(0, null)
        }
    }
}

private fun dayOneA() {
    val lines = "advent/23/input1-a.txt".readFile()
    var total = 0
    lines.forEach {
        var foundNumber = false
        var lastNumber = 0
        it.forEach { c: Char ->
            if (c.toInt() in 48..57) { // is number
                lastNumber = c.toInt() - 48
                if (!foundNumber) { // add 10x number to total
                    total += 10 * lastNumber
                    foundNumber = true
                }
            }
        }
        total += lastNumber
    }
    println(total)
}

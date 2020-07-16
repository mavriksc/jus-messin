package org.mavriksc.messin.hackerrank

//https://www.hackerrank.com/challenges/fair-rations

fun main() {
    fairRations(arrayOf(2, 3, 4, 5, 6))
}

fun fairRations(B: Array<Int>) {
    var breadCount = 0
    B.filterIndexed { idx, _ -> idx < B.lastIndex }
            .forEachIndexed { index, _ ->
                if ((B[index] % 2) ==1) {
                    breadCount += 2
                    B[index]++
                    B[index + 1]++
                }
            }
    if (B[B.lastIndex] % 2 == 0)
        println(breadCount)
    else
        println("NO")
}
package org.mavriksc.messin.hackerrank

import kotlin.math.min

//https://www.hackerrank.com/challenges/3d-surface-area/problem
fun main() {
    print(surfaceArea(arrayOf(arrayOf(1))))
}

fun surfaceArea(A: Array<Array<Int>>): Int {
    return A.mapIndexed { x, list ->
        list.mapIndexed { y, value ->
            var addArea = 6 * value - (2 * (value - 1))
            if (x > 0)
                addArea -= 2 * min(value, A[x - 1][y])
            if (y > 0)
                addArea -= 2 * min(value, A[x][y - 1])
            addArea
        }.sum()
    }.sum()
}
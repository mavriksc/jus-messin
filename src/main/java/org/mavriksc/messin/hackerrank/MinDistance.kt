package org.mavriksc.messin.hackerrank

//https://www.hackerrank.com/challenges/minimum-distances/problem
fun main() {
    print(minimumDistances(arrayOf(1, 2, 3, 4, 10)))
}

fun minimumDistances(a: Array<Int>): Int {
    return a.mapIndexed { index, s -> index to s }
            .groupBy({ it.second }, { it.first })
            .filterValues { it.count() > 1 }
            .map { list ->
                list.value.filterIndexed { index, _ -> index < list.value.size - 1 }
                        .mapIndexed { idx, pos -> list.value[idx + 1] - pos }.min() ?: Int.MAX_VALUE
            }
            .minBy { it } ?: -1
}
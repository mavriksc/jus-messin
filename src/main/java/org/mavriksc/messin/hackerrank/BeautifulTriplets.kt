package org.mavriksc.messin.hackerrank

//https://www.hackerrank.com/challenges/beautiful-triplets/
fun main() {
    print(beautifulTriplets(3, arrayOf(1, 2, 4, 5, 7, 8, 10)))
}

fun beautifulTriplets(d: Int, arr: Array<Int>): Int {
    if (arr[0]+2*d > arr.last())
        return 0

    val things = arr.mapIndexed { index, s -> index to s}.groupBy({it.second},{it.first})

    return  things.keys.filter { it < arr.last() - 2 * d + 1 }
            .filter { things.containsKey(it + d) && things.containsKey(it + 2 * d) }
            .map { (things[it] ?: emptyList()).count() * (things[it+d] ?: emptyList()).count() * (things[it+d*2] ?: emptyList()).count() }
            .sum()
}
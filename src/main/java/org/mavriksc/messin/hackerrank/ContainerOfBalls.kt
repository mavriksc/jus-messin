package org.mavriksc.messin.hackerrank

import java.io.BufferedReader
import java.io.FileReader

// https://www.hackerrank.com/challenges/organizing-containers-of-balls/problem
fun main() {
    val reader = BufferedReader(FileReader("D:\\code\\jus-messin\\src\\main\\resources\\cob\\input01.txt"))
    val q = reader.readLine().trim().toInt()

    for (qItr in 1..q) {
        val n = reader.readLine().trim().toInt()

        val container = Array(n) { Array(n) { 0 } }

        for (i in 0 until n) {
            container[i] = reader.readLine().split(" ").map { it.trim().toInt() }.toTypedArray()
        }

        val result = organizingContainers(container)

        println(result)

    }
}

fun organizingContainers(container: Array<Array<Int>>): String {
    val lookup = Array(container.size) { Array(container.size) { -1 } }
    val homelessBalls = List(container.size) { it }
    val bucketsToFill = List(container.size) { it }
    return if (recSolution(container, bucketsToFill, homelessBalls, lookup))
        "Possible"
    else
        "Impossible"
}


fun recSolution(buckets: Array<Array<Int>>, bucketsToFill: List<Int>, homelessBalls: List<Int>, lookup: Array<Array<Int>>)
        : Boolean {
    if (bucketsToFill.isEmpty() && homelessBalls.isEmpty()) return true
    bucketsToFill.forEach { bucket ->
        homelessBalls.forEach { ball ->
            val possible: Boolean
            if (lookup[bucket][ball] == -1) {
                possible = ballInBucketPossible(ball, bucket, buckets)
                lookup[bucket][ball] = if (possible) 1 else 0
            } else {
                possible = lookup[bucket][ball] > 0
            }
            if (possible) {
                return recSolution(buckets, bucketsToFill.toMutableList().apply { remove(bucket) },
                        homelessBalls.toMutableList().apply { remove(ball) }, lookup)
            }
        }
        return false
    }
    return false
}

fun ballInBucketPossible(ball: Int, bucket: Int, buckets: Array<Array<Int>>): Boolean {
    var ins = 0L
    var outs = 0L
    for (j in buckets.indices) {
        if (j != bucket) {
            ins += buckets[j][ball]
        }
        if (j != ball) {
            outs += buckets[bucket][j]
        }
    }
    if (ins != outs)
        return false
    return true
}

fun permuteListOfLists(list: Array<Array<Int>>, permuteFun: List<Int>) = Array(list.size) { list[permuteFun[it]] }


fun <R> permuteList(list: List<R>): List<List<R>> {
    if (list.size == 1)
        return listOf(list)
    return list.flatMap {
        val tail = list.toMutableList()
        tail.remove(it)
        permuteList(tail).map { tp ->
            mutableListOf(it).apply { addAll(tp) }
        }
    }
}

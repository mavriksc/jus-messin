package org.mavriksc.messin.bs

import kotlin.random.Random

fun main() {
    val list = (0..< 10).map { Random.nextInt(100) }.sorted()
    println(list)
    println("50's index if exists ${bs(list, 50)}")
    val t = list.random()
    println("$t's index if exists ${bs(list, t)}")
}

fun bs(l: List<Int>, target: Int): Int? {
    var left = 0
    var right = l.lastIndex
    var mid: Int
    while (left <= right) {
        mid = (left + right) / 2
        if(l[mid] == target) return mid
        if(l[mid] < target) left = mid + 1
        else right = mid - 1
    }
    return null
}
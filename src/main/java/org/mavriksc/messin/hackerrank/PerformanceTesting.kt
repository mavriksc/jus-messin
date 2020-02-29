package org.mavriksc.messin.hackerrank

import java.util.*
import kotlin.random.Random

const val trials = 524_288
fun main() {

    //Time for mutable list: 503
    //Time for array list: 295
    testArrayFilling()

    // Time for null list: 53
    ////Time for obj then reset list: 33
    ////second only viable with var ¯\_(ツ)_/¯
    checkNullObjectInit()

}

fun checkNullObjectInit() {

    val one = Date()
    val arr1 = Array<SuffixArray.EntRY?>(trials) { null }
    val sb = StringBuilder()
    for (x in arr1.indices) {
        arr1[x] = SuffixArray.EntRY(123, 4352, 4356)
        sb.append(arr1[x]?.nr0)
    }
    val two = Date()

    val three = Date()
    val arr2 = Array<SuffixArray.EntRY>(trials) { SuffixArray.EntRY(0, 0, 0) }
    val sb2 = StringBuilder()
    arr2.forEach {
        it.nr0 = 123
        it.nr1 = 4352
        it.p = 4356
        sb2.append(it.nr0)
    }
    val four = Date()
    println("Time for null list: ${two.time - one.time}")
    println("Time for obj then reset list: ${four.time - three.time}")
    println("second only viable with var ¯\\_(ツ)_/¯")

}

fun testArrayFilling() {
    val strings = genRandomStrings(trials)
    val sb = StringBuilder()
    val one = Date()
    val table = mutableListOf<Int>()
    strings.forEachIndexed { index, str ->
        table.addAll((0..str.length + 1).map { index })
        sb.append(str).append('^')
    }
    val two = Date()

    val three = Date()
    val sCat = strings.joinToString("^")
    var count = 0
    val table2 = Array(sCat.length) { i ->
        if (sCat[i] == '^') {
            count++
            count - 1
        } else
            count
    }

    val four = Date()

    println("Time for mutable list: ${two.time - one.time}")
    println("Time for array list: ${four.time - three.time}")
}

fun genRandomStrings(n: Int): List<String> {
    return (1..n).map { _ -> (1..Random.nextInt(5, 20)).map { _ -> Random.nextInt('a'.toInt(), 'z'.toInt()) }.map { it.toChar() }.joinToString("") }.toList()
}
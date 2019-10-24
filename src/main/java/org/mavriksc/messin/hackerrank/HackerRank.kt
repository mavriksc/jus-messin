package org.mavriksc.messin.hackerrank

import kotlin.math.*

fun main() {
}

fun designerPdfViewer(h: Array<Int>, word: String) = word.length * word.toLowerCase().chars().map { h[it - 'a'.toInt()] }.max().asInt

fun bonAppetit(bill: Array<Int>, k: Int, b: Int) {
    val fair = (bill.sum() - bill[k]) / 2 - b
    if (fair == 0) println("Bon Appetit") else println(fair)
}

fun pageCount(n: Int, p: Int) = min(p / 2, if (n % 2 == 0) ((n - p) + 1) / 2 else (n - p) / 2)


fun countingValleys(n: Int, s: String): Int {
    var height = 0
    var valCount = 0
    var oldHeight = 0
    for (c in s) {
        when (c) {
            'U' -> height++
            'D' -> height--
        }
        if (height == 0 && oldHeight < 0)
            valCount++
        oldHeight = height
    }
    return valCount
}

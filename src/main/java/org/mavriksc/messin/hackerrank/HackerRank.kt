package org.mavriksc.messin.hackerrank

import kotlin.math.*

fun main() {
    val n=10
    println(pageCount(n,10))
    println(pageCount(n,9))
    println(pageCount(n,8))
    println(pageCount(n,7))
    println(pageCount(n,6))
    println(pageCount(n,5))
    println(pageCount(n,4))
    println(pageCount(n,3))
    println(pageCount(n,2))
    println(pageCount(n,1))
}

fun designerPdfViewer(h:Array<Int>,word:String)= word.length * word.toLowerCase().chars().map { h[it-'a'.toInt()] }.max().asInt

fun bonAppetit(bill: Array<Int>, k: Int, b: Int) {
    val fair = (bill.sum()-bill[k])/2 - b
    if (fair==0) println("Bon Appetit") else println(fair)
}

fun pageCount(n: Int, p: Int) =min(p/2, if(n%2==0)((n-p)+1)/2 else (n-p)/2)
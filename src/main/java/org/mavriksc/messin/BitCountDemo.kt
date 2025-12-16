package org.mavriksc.messin

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val l = 0b010101010100101010101010101010101011011010010101010101010100
    val ones = l.countOneBits()
    println(ones)
}
package org.mavriksc.messin.random

data class TestingData(var testing: Int)

fun main() {
    val testing = TestingData(5)
    val s = "testing is ${testing.testing}"
    println(s)
    testing.testing = 10
    println(s)
}
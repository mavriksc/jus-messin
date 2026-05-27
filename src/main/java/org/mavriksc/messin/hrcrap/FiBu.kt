package org.mavriksc.messin.hrcrap

fun main() {
    val rules = mapOf(15 to "FizzBuzz", 5 to "Buzz", 3 to "Fizz")
    val keys = rules.keys.toList().sortedDescending()
    (1..200).forEach {
        val rule = keys.firstOrNull { k -> it % k == 0 }
        rule?.let { r -> println(rules[r]) } ?: println(it)
    }
}
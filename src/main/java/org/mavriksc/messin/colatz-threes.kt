package org.mavriksc.messin

import java.io.File

private const val redColor = "\u001b[31m"
private const val greenColor = "\u001b[32m"
private const val reset = "\u001b[0m" // Resets color to default
fun main() {
    //someNumbers()
    leastEvenNumbers()
    ofLeastEvenNumbers()
}

//outputs even numbers in red if they can't be gotten to from odd numbers
// green if they can and the odd number source
fun someNumbers() =
    (1..100).map { it * 2 }.forEach {
        if ((it - 1) % 3 == 0)
            println("$greenColor$it - ${(it - 1) / 3}$reset")
        else println("$redColor$it$reset")
    }

// generate a file with the least even numbers
fun leastEvenNumbers() =
    File("leastEvenNumbers.txt")
        .writeText(
            (1..10000)
                .filter { it % 2 == 1 }
                .map { it * 2 }
                .joinToString("\n")
        )

fun ofLeastEvenNumbers() {
    val nums = File("leastEvenNumbers.txt").readLines().map { it.toInt() }
    nums.filter { nums.contains((it * 3 + 1) / 2) }.forEach { println(it) }
}
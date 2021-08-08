package org.mavriksc.messin

import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    //println(highestPowerOf2(448))
    sieveOfEratosthenes(30)
}

fun highestPowerOf2(n: Int): Int = n and (n - 1).inv()

//https://www.geeksforgeeks.org/sieve-of-eratosthenes/
fun sieveOfEratosthenes(n: Int) {
    // Create a boolean array
    // "prime[0..n]" and
    // initialize all entries
    // it as true. A value in
    // prime[i] will finally be
    // false if i is Not a
    // prime, else true.
    val prime = BooleanArray(n + 1) { true }
    val limit = floor(sqrt(n.toDouble())).toInt()
    for (x in 2..limit){

    }
    var p = 2
    while (p * p <= n) {
        if (prime[p]) {
            for (i in p * p..n step p) {
                prime[i] = false
            }
        }
        p++
    }

    // Print all prime numbers
    for (i in 2..n) {
        if (prime[i]) print("$i ")
    }
}

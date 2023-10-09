package org.mavriksc.messin

import java.io.File
import java.math.BigInteger


val writeToPrimeFile = File("primes2.txt")
val chunkSize = BigInteger("20000")
val N = BigInteger("10000000")

fun main() {
    var chunkNum = BigInteger("0")
    while (chunkNum * chunkSize < N) {
        writeToPrimeFile.appendText(
            "\n" + segmentedSieve(
                chunkNum * chunkSize,
                ++chunkNum * chunkSize - BigInteger.ONE
            ).joinToString("\n")
        )
        chunkNum++
    }

}

//https://www.geeksforgeeks.org/sieve-of-eratosthenes/
fun sieveOfEratosthenes(n: Int): List<Int> {
    // Create a boolean array
    // "prime[0..n]" and
    // initialize all entries
    // it as true. A value in
    // prime[i] will finally be
    // false if i is Not a
    // prime, else true.
    val sieve = BooleanArray(n + 1) { true }
    val primes = mutableListOf<Int>()
    for (p in 2..n) {
        if (sieve[p]) {
            primes.add(p)
            for (i in p * p..n step p) {
                sieve[i] = false
            }
        }
    }
    return primes
}


fun segmentedSieve(firstValue: BigInteger, lastValue: BigInteger): List<BigInteger> {
    val sieve = BooleanArray((lastValue - firstValue).toInt() + 1) { true }
    val primes = mutableListOf<BigInteger>()
    var current = BigInteger("2")
    while (current < lastValue) {
        // number is in the interval it may be prime check and then do the sweep
        if (firstValue < current && sieve[(current - firstValue).toInt()]) {
            primes.add(current)
        }
        var multiplier = firstValue / current + BigInteger.ONE
        while (multiplier <= lastValue / current) {
            sieve[((current * multiplier) - firstValue).toInt()] = false
            multiplier++
        }
        current++
    }

    return primes
}

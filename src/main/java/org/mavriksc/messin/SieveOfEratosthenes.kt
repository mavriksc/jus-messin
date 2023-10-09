package org.mavriksc.messin

import java.io.File
import java.math.BigInteger

// This works. there was 1 off in the outer while of the segment 

val writeToPrimeFile = File("primes2.txt")
val chunkSize = BigInteger("200000")
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
    }
}

fun segmentedSieve(firstValue: BigInteger, lastValue: BigInteger): List<BigInteger> {
    val sieve = BooleanArray((lastValue - firstValue).toInt() + 1) { true }
    val primes = mutableListOf<BigInteger>()
    var current = BigInteger("2")
    while (current <= lastValue) {
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

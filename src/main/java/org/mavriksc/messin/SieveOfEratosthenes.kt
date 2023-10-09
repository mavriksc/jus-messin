package org.mavriksc.messin

import java.io.File
import java.math.BigInteger

// This works. there was 1 off in the outer while of the segment

val writeToPrimeFile = File("primes2.txt")
val chunkSize = BigInteger("10000")
val N = BigInteger("10000000")
val primez = mutableListOf<BigInteger>()

fun main() {
    var chunkNum = BigInteger("0")
    while (chunkNum * chunkSize < N) {
        primez.addAll(segmentedSieve(chunkNum * chunkSize, ++chunkNum * chunkSize - BigInteger.ONE))
    }
    writeToPrimeFile.appendText(primez.joinToString("\n"))
}

// can make this faster by only sweeping already found primes not every integer
// but only after the first segments primes have been found
fun segmentedSieve(firstValue: BigInteger, lastValue: BigInteger): List<BigInteger> {
    val sieve = BooleanArray((lastValue - firstValue).toInt() + 1) { true }
    return if (firstValue == BigInteger.ZERO) {
        val primes = mutableListOf<BigInteger>()
        var current = BigInteger("2")
        while (current <= lastValue) {
            // number is in the interval it may be prime check and then do the sweep
            if (sieve[(current - firstValue).toInt()]) {
                primes.add(current)
            }
            var multiplier = firstValue / current + BigInteger.ONE
            while (multiplier <= lastValue / current) {
                sieve[((current * multiplier) - firstValue).toInt()] = false
                multiplier++
            }
            current++
        }
        primes
    } else {
        primez.forEach {
            var multiplier =
                if (firstValue % it == BigInteger.ZERO) firstValue / it else firstValue / it + BigInteger.ONE
            while (multiplier <= lastValue / it) {
                sieve[((it * multiplier) - firstValue).toInt()] = false
                multiplier++
            }
        }
        sieve.mapIndexed { i, b -> if (b) firstValue + i.toBigInteger() else null }.filterNotNull()
    }

}

package org.mavriksc.messin

import java.io.File
import java.math.BigInteger

// This works. there was 1 off in the outer while of the segment

val writeToPrimeFile = File("primes2.txt")
val N = BigInteger("1000000000")
val chunkSize = N.sqrt()

fun main() {
    println(chunkSize)
    var chunkNum = BigInteger("1")
    val firstChunkPrimes = firstChunkPrimes(chunkSize - BigInteger.ONE)
    writeToPrimeFile.appendText(firstChunkPrimes.joinToString("\n"))
    while (chunkNum * chunkSize < N) {
        writeToPrimeFile.appendText("\n"+(
            segmentedSieve(
                chunkNum * chunkSize,
                min(++chunkNum * chunkSize - BigInteger.ONE, N),
                firstChunkPrimes
            ).joinToString("\n")
        ))
    }
}

// can make this faster by only sweeping already found primes not every integer----DONE but can do better
// but only after the first segments primes have been found----DONE
// actually can go even faster do first sqrt floor N and then only sweep those primes not all accumulated---DONE
// refactored to make it easier to do this but----DONE
// still need to get and keep first list separate and then sweep the rest--DONE
fun segmentedSieve(firstValue: BigInteger, lastValue: BigInteger, fcp: List<BigInteger>): List<BigInteger> {
    val sieve = BooleanArray((lastValue - firstValue).toInt() + 1) { true }
    fcp.forEach {
        var multiplier =
            if (firstValue % it == BigInteger.ZERO) firstValue / it else firstValue / it + BigInteger.ONE
        while (multiplier <= lastValue / it) {
            sieve[((it * multiplier) - firstValue).toInt()] = false
            multiplier++
        }
    }
    return sieve.mapIndexed { i, b -> if (b) firstValue + i.toBigInteger() else null }.filterNotNull()
}

private fun firstChunkPrimes(lastValue: BigInteger): List<BigInteger> {
    val sieve = BooleanArray(lastValue.toInt() + 1) { true }
    val primes = mutableListOf<BigInteger>()
    var current = BigInteger("2")
    while (current <= lastValue) {
        if (sieve[(current).toInt()]) {
            primes.add(current)
        }
        var multiplier = BigInteger.ONE
        while (multiplier <= lastValue / current) {
            sieve[(current * multiplier).toInt()] = false
            multiplier++
        }
        current++
    }
    return primes
}

fun min(a: BigInteger, b: BigInteger): BigInteger = if (a <= b) a else b

fun BigInteger.sqrt(): BigInteger {
    var div = BigInteger.ZERO.setBit(this.bitLength() / 2)
    var div2 = div
    while (true) {
        val y = div.add(this.divide(div)).shiftRight(1)
        if (y == div || y == div2) return y
        div2 = div
        div = y
    }
}

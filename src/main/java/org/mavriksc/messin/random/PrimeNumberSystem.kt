package org.mavriksc.messin.random

import java.math.BigInteger

fun BigInteger.pow(exponent: BigInteger): BigInteger {
    var res = BigInteger.ONE
    var base = this
    var exp = exponent
    val TWO = BigInteger.valueOf(2)
    while (exp > BigInteger.ZERO) {
        if (exp.remainder(TWO) == BigInteger.ONE) res *= base
        base *= base
        exp = exp.divide(TWO)
    }
    return res
}

fun main() {
    (2..1000).forEach { source ->
        val factor = factor(source.toBigInteger())
        val newNum = factor.map {
            it.power * BigInteger.TEN.pow(BigInteger.valueOf(primes.indexOf(it.base).toLong()))
        }.fold(BigInteger.ZERO) { acc, bigInteger -> acc + bigInteger }
        println("$source -> $newNum")
    }
}

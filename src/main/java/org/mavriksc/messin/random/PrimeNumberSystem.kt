package org.mavriksc.messin.random

import kotlin.math.pow

fun main() {
    (2..100).forEach { source ->
        val factor = factor(source)
        println("$source -> $factor")
        val newNum = factor.map { it.power * 10.0.pow(primes.indexOf(it.base).toDouble()) }.sum().toInt()
        println("$source -> ${newNum.toString().padStart(4, '0')}")
    }
}

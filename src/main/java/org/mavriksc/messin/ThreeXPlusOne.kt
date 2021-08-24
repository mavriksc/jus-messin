package org.mavriksc.messin

import java.io.File
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

const val primeFile = "D:\\code\\jus-messin\\src\\main\\resources\\primes.txt"
const val n = 10_000
val primes = sieveOfEratosthenes(n)
val map = mutableMapOf<Int, MNode>()

fun main() {

    val randNumber = primes.take(7).map { it.toDouble().pow(ThreadLocalRandom.current().nextInt(0, 3)).toInt() }
        .fold(1) { a, i -> a * i }
        .also { println(it) }

    factor(randNumber).forEach { println(it) }

    (2..100).forEach {
        if (!map.containsKey(it))
            doThreeXPlusOne(it)

    }


}

fun doThreeXPlusOne(start: Int) {
    var current = start
    var previous = 0
    var next = 0
    do {
        map[current] = MNode(current, factor(current), previous)
        next = if (current % 2 == 0)
            current / 2
        else
            3 * current + 1
        previous = current
        current = next
    } while (next != 4 || next != 2 || next != 1 || !map.containsKey(next))
    if (map.containsKey(current) && map[current]?.previous == 0)
        map[current]?.previous = previous

}

fun highestPowerOf2(n: Int): Int = n and (n - 1).inv()

//https://www.geeksforgeeks.org/sieve-of-eratosthenes/
fun sieveOfEratosthenes(n: Int): Set<Int> {
    // Create a boolean array
    // "prime[0..n]" and
    // initialize all entries
    // it as true. A value in
    // prime[i] will finally be
    // false if i is Not a
    // prime, else true.
    val prime = BooleanArray(n + 1) { true }
    val set = mutableSetOf<Int>()
    for (p in 2..n) {
        if (prime[p]) {
            set.add(p)
            for (i in p * p..n step p) {
                prime[i] = false
            }
        }
    }
    return set
}

fun factor(number: Int): Set<Factor> =
    primes.filter { it <= number / 2 && number % it == 0 }.map { Factor(it, getPower(it, number)) }.toSet()

fun getPower(base: Int, number: Int): Int {
    var x = number / base;
    var pow = 1;
    while (x % base == 0) {
        x /= base
        pow++
    }
    return pow
}

data class MNode(val number: Int, val factors: Set<Factor>, var previous: Int)
data class Factor(val base: Int, val power: Int)

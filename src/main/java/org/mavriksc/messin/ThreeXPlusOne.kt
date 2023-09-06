package org.mavriksc.messin

import java.io.File

val primeFile = File("primes.txt")
val factorsFile = File("factors.txt")
const val n = 46_000
val primes = primeFile.readText().split(",").map { it.toInt() }//sieveOfEratosthenes(n)
val map = mutableMapOf<Int, MNode>()


// ok so we have primes and factor function that are effecient.

// then just run it on numbers up to n and store all the factors for those numbers
// build the 3x+1 tree and then look at the factors as you move down branches of the tree
// do any branches or segments of branches have paterns in the progression of factors
// conjecture is equivalent to set of factors will eventually only contain a power of 2
fun main() {

    //primeFile.writeText(primes.joinToString(","))

    (4..n).forEach {
        if (!primes.contains(it)) {
            val factors = factor(it).map { f -> "${f.base}^${f.power}" }.joinToString(" * ")
            println("$it : $factors")
        } else println("$it : PRIME")
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

fun factor(number: Int): Set<Factor> =
    primes.take(primes.indexOfFirst { it > number / 2 }).filter { number % it == 0 }
        .map { Factor(it, getPower(it, number)) }.toSet()

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

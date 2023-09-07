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


//building of tree is done. need to point to parent  and then look at factors moving from the leaves to root


fun main() {
    buildTree(15)

}

fun buildTree(depth: Int) {
    val root = TNode(1, null)
    val leaves = mutableListOf<TNode>()
    leaves.add(root)
    (0..depth).forEach { _ ->
        val curLeaves = leaves.toList()
        leaves.clear()
        curLeaves.forEach { leaf ->
            leaf.calcChildren()
            leaves.addAll(leaf.getChildren())
        }
    }

    leaves.clear()
    leaves.add(root)
    do {
        val curLeaves = leaves.toList()
        leaves.clear()
        curLeaves.forEach {
            println("${it.number}: ${it.factors.joinToString(" * ") { f -> "${f.base}^${f.power}" }}")
            println("Children : ${it.getChildren().joinToString(",") { ch -> ch.number.toString() }}")
            leaves.addAll(it.getChildren())
        }

    } while (leaves.isNotEmpty())

}

private fun factorNumbers() {
    (4..n).forEach {
        if (!primes.contains(it)) {
            val factors = factor(it).joinToString(" * ") { f -> "${f.base}^${f.power}" }
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

class TNode(val number: Int, val parent: TNode?) {
    val factors = factor(number)
    var twoTimes: TNode? = null
        private set
    var minusOneDivThree: TNode? = null
        private set

    fun calcChildren() {
        twoTimes = TNode(number * 2, this)
        if ((number - 1) % 3 == 0 && (number - 1) / 3 > 1) minusOneDivThree = TNode((number - 1) / 3, this)
    }

    fun getChildren() = listOfNotNull(twoTimes, minusOneDivThree)
}

data class MNode(val number: Int, val factors: Set<Factor>, var previous: Int)
data class Factor(val base: Int, val power: Int)

package org.mavriksc.messin.random

import java.io.File

import java.math.BigInteger

val primeFile = File("primes.txt")
val factorsFile = File("factors.txt")
const val n = 46_000
val primes = primeFile.readText().split(",").map { it.trim().toBigInteger() }
val map = mutableMapOf<Int, MNode>()


// ok so we have primes and factor function that are efficient.

// then just run it on numbers up to n and store all the factors for those numbers
// build the 3x+1 tree and then look at the factors as you move down branches of the tree
// do any branches or segments of branches have patterns in the progression of factors
// conjecture is equivalent to set of factors will eventually only contain a power of 2

//building of tree is done. need to point to parent  and then look at factors moving from the leaves to root

// ANY NODE THAT IS A MULTIPLE OF 3 WILL ONLY HAVE 2x children forever

// ULong wasn't very good
// use segmented sieve to get more primes--DONE
// TODO Try BigInteger

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

fun doThreeXPlusOne(start: Int) {
    var current = start
    var previous = 0
    var next: Int
    do {
        map[current] = MNode(current, factor(current.toBigInteger()), previous)
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

// powers of 1 arent working but maybe only for low numbers. use filter instead of take to get the right answer and improve later
fun factor(number: BigInteger): Set<Factor> =
    primes.takeWhile { it <= number }.filter { number % it == BigInteger.ZERO }
        .map { Factor(it, getPower(it, number)) }.toSet()

fun getPower(base: BigInteger, number: BigInteger): BigInteger {
    var x = number / base
    var pow = BigInteger.ONE
    while (x % base == BigInteger.ZERO) {
        x /= base
        pow++
    }
    return pow
}

class TNode(val number: Int, val parent: TNode?) {
    val factors = factor(number.toBigInteger())
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
data class Factor(val base: BigInteger, val power: BigInteger)

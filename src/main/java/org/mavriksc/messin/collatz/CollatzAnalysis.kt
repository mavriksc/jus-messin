package org.mavriksc.messin.collatz

import java.io.File
import java.math.BigInteger

private val TWO = BigInteger.valueOf(2)
private val THREE = BigInteger.valueOf(3)
private val TEN = BigInteger.TEN
private val primesFile = File("primes.txt")
private val extendedPrimesFile = File("primes2.txt")

private val primeCache: MutableList<BigInteger> by lazy {
    primesFile.readText()
        .split(Regex("[,\\s]+"))
        .filter { it.isNotBlank() }
        .map { it.toBigInteger() }
        .toMutableList()
}

private val primeIndexCache: MutableMap<BigInteger, Int> by lazy {
    primeCache.withIndex().associate { it.value to it.index }.toMutableMap()
}

data class FactorPower(val prime: BigInteger, val exponent: Int)

data class TowerInfo(val oddCore: Long, val height: Int)

data class ReverseParentInfo(
    val fromAbove: Long,
    val oddParent: Long?
)

data class CollatzStepRecord(
    val stepIndex: Int,
    val value: Long,
    val parity: String,
    val factorization: String,
    val primeNumberSystem: BigInteger,
    val oddCore: Long,
    val towerHeight: Int,
    val nextValue: Long?,
    val moveType: String
)

data class TowerTransitionRecord(
    val sourceOdd: Long,
    val sourceFactorization: String,
    val sourcePrimeNumberSystem: BigInteger,
    val rawThreeXPlusOne: Long,
    val destinationOddCore: Long,
    val absorbedTowerHeight: Int
)

data class DualParentEvenRecord(
    val evenNumber: Long,
    val oddPredecessor: Long,
    val oddCore: Long,
    val towerHeight: Int
)

data class PersistedCollatzNode(
    val number: Long,
    val definiteConnectedNumber: Long,
    val optionalConnectedNumber: Long?,
    val distanceFromRoot: Int,
    val towerHeight: Int,
    val factorCount: Int,
    val primeFactorization: String,
    val primeNumberSystem: BigInteger
)

fun collatzNext(value: Long): Long =
    if (value % 2L == 0L) value / 2L else value * 3L + 1L

fun collatzNext(value: BigInteger): BigInteger =
    if (value.mod(TWO) == BigInteger.ZERO) value.divide(TWO) else value.multiply(THREE).add(BigInteger.ONE)

fun towerInfo(value: Long): TowerInfo {
    require(value > 0) { "Tower info is only defined for positive values." }
    var oddCore = value
    var height = 0
    while (oddCore % 2L == 0L) {
        oddCore /= 2L
        height++
    }
    return TowerInfo(oddCore = oddCore, height = height)
}

fun reverseParentInfo(value: Long): ReverseParentInfo {
    val oddParentCandidate = if (
        value > 1 &&
        value % 2L == 0L &&
        (value - 1L) % 3L == 0L
    ) {
        val predecessor = (value - 1L) / 3L
        predecessor.takeIf { it > 0 && it % 2L == 1L }
    } else {
        null
    }

    return ReverseParentInfo(
        fromAbove = value * 2L,
        oddParent = oddParentCandidate
    )
}

fun factorize(value: Long): List<FactorPower> {
    require(value > 0) { "Factorization is only defined for positive values." }
    var remaining = value.toBigInteger()
    val factors = mutableListOf<FactorPower>()
    for (prime in primeCache) {
        if (prime.multiply(prime) > remaining) {
            break
        }
        if (remaining.mod(prime) == BigInteger.ZERO) {
            var exponent = 0
            while (remaining.mod(prime) == BigInteger.ZERO) {
                remaining = remaining.divide(prime)
                exponent++
            }
            factors += FactorPower(prime = prime, exponent = exponent)
        }
    }

    if (remaining > BigInteger.ONE) {
        factors += FactorPower(prime = remaining, exponent = 1)
    }

    return factors
}

fun factorizationString(value: Long): String =
    factorize(value)
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" * ") { "${it.prime}^${it.exponent}" }
        ?: "1"

fun factorCount(value: Long): Int = factorize(value).size

fun factorSignature(value: Long, maxShownFactors: Int = 3): String {
    val factors = factorize(value)
        .filterNot { it.prime == TWO }
        .take(maxShownFactors)
    return if (factors.isEmpty()) {
        "1"
    } else {
        factors.joinToString(" * ") { "${it.prime}^${it.exponent}" }
    }
}

fun primeNumberSystemValue(value: Long): BigInteger {
    return factorize(value).fold(BigInteger.ZERO) { acc, factor ->
        val index = primeIndex(factor.prime)
        acc + BigInteger.valueOf(factor.exponent.toLong()) * TEN.pow(index)
    }
}

private fun primeIndex(prime: BigInteger): Int {
    primeIndexCache[prime]?.let { return it }
    extendPrimeCacheThrough(prime)
    return primeIndexCache[prime]
        ?: error("Prime $prime could not be indexed.")
}

private fun extendPrimeCacheThrough(targetPrime: BigInteger) {
    extendPrimeCacheFromFile(targetPrime)
    if (primeCache.last() >= targetPrime) {
        return
    }

    var candidate = primeCache.last().add(BigInteger.ONE)
    if (candidate.mod(TWO) == BigInteger.ZERO) {
        candidate = candidate.add(BigInteger.ONE)
    }

    while (primeCache.last() < targetPrime) {
        if (isPrime(candidate)) {
            primeIndexCache[candidate] = primeCache.size
            primeCache += candidate
        }
        candidate = candidate.add(TWO)
    }
}

private fun extendPrimeCacheFromFile(targetPrime: BigInteger) {
    if (!extendedPrimesFile.exists()) {
        return
    }

    var currentMax = primeCache.last()
    if (currentMax >= targetPrime) {
        return
    }

    extendedPrimesFile.useLines { lines ->
        for (line in lines) {
            val value = line.trim()
            if (value.isEmpty()) {
                continue
            }
            val prime = value.toBigInteger()
            if (prime <= currentMax) {
                continue
            }
            primeIndexCache[prime] = primeCache.size
            primeCache += prime
            currentMax = prime
            if (prime >= targetPrime) {
                break
            }
        }
    }
}

private fun isPrime(candidate: BigInteger): Boolean {
    if (candidate < TWO) {
        return false
    }
    for (prime in primeCache) {
        if (prime.multiply(prime) > candidate) {
            break
        }
        if (candidate.mod(prime) == BigInteger.ZERO) {
            return false
        }
    }
    return true
}

fun buildTowerTransition(sourceOdd: Long): TowerTransitionRecord {
    require(sourceOdd > 0 && sourceOdd % 2L == 1L) { "Tower transitions require a positive odd source." }
    val raw = sourceOdd * 3L + 1L
    val destinationTower = towerInfo(raw)
    return TowerTransitionRecord(
        sourceOdd = sourceOdd,
        sourceFactorization = factorizationString(sourceOdd),
        sourcePrimeNumberSystem = primeNumberSystemValue(sourceOdd),
        rawThreeXPlusOne = raw,
        destinationOddCore = destinationTower.oddCore,
        absorbedTowerHeight = destinationTower.height
    )
}

fun buildDualParentEvenRecord(value: Long): DualParentEvenRecord? {
    val reverseParent = reverseParentInfo(value)
    val oddParent = reverseParent.oddParent ?: return null
    val tower = towerInfo(value)
    return DualParentEvenRecord(
        evenNumber = value,
        oddPredecessor = oddParent,
        oddCore = tower.oddCore,
        towerHeight = tower.height
    )
}

fun optionalConnectedNumber(value: Long): Long? = reverseParentInfo(value).oddParent

fun buildPersistedNode(value: Long, distanceFromRoot: Int): PersistedCollatzNode {
    val tower = towerInfo(value)
    return PersistedCollatzNode(
        number = value,
        definiteConnectedNumber = value * 2L,
        optionalConnectedNumber = optionalConnectedNumber(value),
        distanceFromRoot = distanceFromRoot,
        towerHeight = tower.height,
        factorCount = factorCount(value),
        primeFactorization = factorizationString(value),
        primeNumberSystem = primeNumberSystemValue(value)
    )
}

fun buildTrajectory(seed: Long): List<CollatzStepRecord> {
    require(seed > 0) { "Trajectory requires a positive seed." }
    val records = mutableListOf<CollatzStepRecord>()
    var current = seed
    var stepIndex = 0
    while (true) {
        val nextValue = if (current == 1L) null else collatzNext(current)
        val moveType = when {
            current == 1L -> "terminal"
            current % 2L == 0L -> "tower-descent"
            else -> "odd-jump"
        }
        val tower = towerInfo(current)
        records += CollatzStepRecord(
            stepIndex = stepIndex,
            value = current,
            parity = if (current % 2L == 0L) "even" else "odd",
            factorization = factorizationString(current),
            primeNumberSystem = primeNumberSystemValue(current),
            oddCore = tower.oddCore,
            towerHeight = tower.height,
            nextValue = nextValue,
            moveType = moveType
        )
        if (current == 1L) {
            break
        }
        current = nextValue!!
        stepIndex++
    }
    return records
}

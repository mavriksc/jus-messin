package org.mavriksc.messin.advent.twentyfive

import org.mavriksc.messin.random.readFile

fun main() {

    val sampleOrInput = "sample"
    val input = "advent/five/${sampleOrInput}2.txt".readFile()[0]
    dayTwoPartOne(input)
    dayTwoPartTwo(input)
}


fun dayTwoPartOne(input: String) {
    val count = input.split(",").sumOf {
        val ids = it.split("-")
        sumInvalidInRange(ids[0], ids[1])
    }
    println(count)
}

fun sumInvalidInRange(start: String, end: String): Long {
    // if start is odd length fist is one longer "10..."
    // if end is odd length last is one shorter and "99.."
    val adjustedStart = if (start.length % 2 == 0) start else "1${"0".repeat(start.length)}"
    val adjustedEnd = if (end.length % 2 == 0) end else "9".repeat(end.length - 1)

    val partMinLen = adjustedStart.length / 2
    val partMaxLen = adjustedEnd.length / 2
    // even length strings composed of AB and CD
    // where len A = len B and len C = len D
    // when starting the count
    // if A>B then AA is first patern match
    // else A+1A+1 is the first
    // when approaching end of range
    // if C<D then CC is the last patern match
    // else C-1C-1 is the last

    val a = adjustedStart.substring(0, partMinLen).toLong()
    val b = adjustedStart.substring(partMinLen, adjustedStart.length).toLong()
    val c = adjustedEnd.substring(0, partMaxLen).toLong()
    val d = adjustedEnd.substring(partMaxLen, adjustedEnd.length).toLong()
    val first = if (a >= b) a else a + 1L
    val last = if (c <= d) c else c - 1L

    return (first..last).sumOf { it.toString().repeat(2).toLong() }
}

// Part 2 patterns of all lengths. which would be primes. there will be dupes will need a set and then sum. on length 6 you can get 11,11,11  and 111,111
// basically any place there is a 2 we want to pass in primes up to the length of the string
// the startOdd and endOdd. need to be reversed into the nearest divisible length. in the right direction
// if (start.length % prime !=0) "1${"0".repeat((((start.length / prime)+1)*prime)-1)}" else start
// if (end.length % prime !=0) "0".repeat(((end.length / prime)-1)*prime) else end
// chunk start and end into strings of length prime.
// for this we will call A start and A[0],A[1],A[2]... the chunks
// and C end and C[0],C[1],C[2]... the chunks
// if A[0] is the biggest start with A[0] else A[0]+1
// if C[0] is the smallest end with C[0] else C[0]-1

fun dayTwoPartTwo(input: String) {
    val maxLength = Long.MAX_VALUE.toString().length
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)
    val invalidIds = input.split(",").flatMap {
        val idRange = it.split("-")
        primes
            .filter { p -> p < maxLength && p <= idRange[1].length }
            .flatMap { p -> invalidInRange(idRange[0], idRange[1], p) }
    }.toSet()
    invalidIds.forEach { println(it) }
    println(invalidIds.sum())
}

fun invalidInRange(start: String, end: String, numOfReps: Int): List<Long> {
    //println("start: $start, end: $end, reps: $numOfReps")
    val adjustedStart =
        if (start.length % numOfReps == 0) start else "1${"0".repeat(((start.length / numOfReps + 1) * numOfReps) - 1)}"
    val adjustedEnd = if (end.length % numOfReps == 0) end else "9".repeat((end.length / numOfReps) * numOfReps)
    if (adjustedEnd.isEmpty() || adjustedStart.toLong() > adjustedEnd.toLong()) return emptyList()

    val startPart = adjustedStart.take(adjustedStart.length / numOfReps)
    val endPart = adjustedEnd.take(adjustedEnd.length / numOfReps)
    val start = if (startPart.repeat(numOfReps).toLong()>=adjustedStart.toLong()) startPart.toLong() else startPart.toLong() + 1
    val end = if (endPart.repeat(numOfReps).toLong() <= adjustedEnd.toLong()) endPart.toLong() else endPart.toLong() - 1
    val ans = (start..end).map { it.toString().repeat(numOfReps).toLong() }
    //println(ans.joinToString(", "))
    return ans
}


fun dayTwoPartTwoDumbAFVersion(input: String) {
    val maxLength = Long.MAX_VALUE.toString().length
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)
    val invalidIds = input.split(",").flatMap {
        val idRange = it.split("-")
        invalidInRangeDumbAFVersion(idRange[0], idRange[1], primes)
    }.toSet()
    println(invalidIds.sum())
}

fun invalidInRangeDumbAFVersion(start: String, end: String, primes: List<Int>): List<Long> {
    val filteredPrimes = primes.filter { it <= end.length }
    (start.toLong()..end.toLong()).filter { filteredPrimes.any { prime -> it.toString().contains(prime.toString()) } }

    return emptyList()
}

package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.toReader
import kotlin.math.abs


val working = mutableListOf<Boolean>()
val notWorking = mutableListOf<Boolean>()

fun main() {
    dayTwoPartOne()
    dayTwoPartOneRecursive()
    dayTwoParTwoSanity()
    dayTwoPartTwo()

    d2p2()
}

fun dayTwoPartOne() {
    "advent/24/2/input".toReader().useLines { lines ->
        val t = lines.map { line ->
            line.split(" ")
                .map { it.toInt() }
        }
            .filter {
                isValid(it)
            }.count()
        println(t)
    }
}

fun dayTwoParTwoSanity() {
    "advent/24/2/input".toReader().useLines { lines ->
        val t = lines.map { line ->
            line.split(" ")
                .map { it.toInt() }
        }
            .filter {
                val fil = isSafeCanWithstand(it, 1)
                working.add(fil)
                fil
            }
            .count()
        println(t)
    }

}

fun dayTwoPartOneRecursive() {
    "advent/24/2/input".toReader().useLines { lines ->
        val t = lines.map { line ->
            line.split(" ")
                .map { it.toInt() }
        }
            .filter {
                val fil = isSafeTailed(it[0], it[1], it.drop(2), 0)
                working.add(fil)
                fil
            }
            .count()
        println(t)
    }
}
// for part 2
// if hit false need to check if either removing n-1  meaning (n-2 ->n) is valid as well as the rest of the list
// or remove n and then (n-1 -> n+1 ) is valid as well as the rest of the list
// havent got it right yet. if i set that there is an error already it will give the same list as part 1 seems right but not correct.


fun isValid(report: List<Int>): Boolean {
    if (report[0] == report[1]) {
        return false
    }
    val isIncreasing = report[0] < report[1]
    report.drop(1).forEachIndexed { index, current ->
        val prior = report[index]
        //println("Is increasing:$isIncreasing Current:$current Prior:$prior")
        if (isIncreasing) {
            if (current <= prior || current - prior > 3)
                return false
        } else if (current >= prior || prior - current > 3)
            return false
    }
    return true
}

fun isSafeCanWithstand(report: List<Int>, canWithstand: Int): Boolean {
    if (report[0] == report[1]) {
        return if (canWithstand > 0)
            isSafeCanWithstand(report.drop(1), canWithstand - 1) ||
                    isSafeCanWithstand(listWithElementAtRemoved(1, report), canWithstand - 1)
        else false
    }
    val isIncreasing = report[0] < report[1]
    report.drop(1).forEachIndexed { index, current ->
        val prior = report[index]
        //println("Is increasing:$isIncreasing Current:$current Prior:$prior")
        if (isIncreasing) {
            if (current <= prior || current - prior > 3) {
                return if (canWithstand > 0)
                    isSafeCanWithstand(listWithElementAtRemoved(index, report), canWithstand - 1) ||
                            isSafeCanWithstand(listWithElementAtRemoved(index + 1, report), canWithstand - 1)
                else false
            }
        } else if (current >= prior || prior - current > 3) {
            return if (canWithstand > 0)
                isSafeCanWithstand(listWithElementAtRemoved(index, report), canWithstand - 1) ||
                        isSafeCanWithstand(listWithElementAtRemoved(index + 1, report), canWithstand - 1)
            else false
        }
    }
    return true
}

fun <T> listWithElementAtRemoved(index: Int, list: List<T>): List<T> {
    val newList = list.toMutableList()
    newList.removeAt(index)
    return newList
}

fun isSafeTailed(current: Int, next: Int, tail: List<Int>, increasing: Int): Boolean {
    val difference = next - current
    if (difference == 0 || abs(difference) > 3) return false
    // establish increasing
    if (increasing == 0)
        return isSafeTailed(current, next, tail, difference)
    //still need to check the direction
    if (increasing > 0 && difference < 0 || increasing < 0 && difference > 0) return false
    if (tail.isNotEmpty()) {
        val newNext = tail.first()
        val newTail = tail.drop(1)
        return isSafeTailed(next, newNext, newTail, increasing)
    } else return true
}

fun isSafeTailedHandleOneError(
    current: Int,
    next: Int,
    tail: List<Int>,
    increasing: Int,
    hasError: Boolean,
    last: Int
): Boolean {
    val difference = next - current
    if (difference == 0 || abs(difference) > 3) return false
    // establish increasing
    if (increasing == 0) {
        //cant remove current from this case only next and try again <- false can remove current and do with next and a new new next
        val isSafe = isSafeTailedHandleOneError(current, next, tail, difference, hasError, -1)
        if (!isSafe) {
            if (hasError) return false
            else {
                val newNext = tail.first()
                val newTail = tail.drop(1)
                fun validIfDropCurrent(): Boolean {
                    return isSafeTailedHandleOneError(next, newNext, newTail, newNext - next, true, -1)
                }

                fun validIfDropNext(): Boolean {
                    return isSafeTailedHandleOneError(current, newNext, newTail, newNext - current, true, -1)
                }

                return validIfDropCurrent() || validIfDropNext()
            }
        }
    }
    //still need to check the direction
    if (increasing > 0 && difference < 0 || increasing < 0 && difference > 0) return false
    if (tail.isNotEmpty()) {
        val newNext = tail.first()
        val newTail = tail.drop(1)
        val isSafe = isSafeTailedHandleOneError(next, newNext, newTail, increasing, hasError, current)
        if (!isSafe) {
            if (hasError) return false
            else {
                fun validIfDropCurrent(): Boolean {
                    return last != -1 && isSafeTailedHandleOneError(last, next, tail, increasing, true, -1)
                }

                fun validIfDropNext(): Boolean {
                    return newTail.isNotEmpty() && isSafeTailedHandleOneError(
                        current,
                        newTail.first(),
                        newTail.drop(1),
                        increasing,
                        true,
                        last
                    )
                }
                return validIfDropCurrent() || validIfDropNext()
            }
        }
    }
    return true
}


fun dayTwoPartTwo() {
    "advent/24/2/input".toReader().useLines { lines ->
        val t = lines.map { line ->
            line.split(" ")
                .map { it.toInt() }
        }
            .filter {
                val fil = isSafeTailedHandleOneError(it[0], it[1], it.drop(2), 0, false, -1)
                notWorking.add(fil)
                fil
            }
            .count()
        println(t)
    }
}

fun d2p2() {
    val failed = mutableListOf<List<Int>>()
    val firstPass = "advent/24/2/input".toReader().useLines { lines ->
        lines.map { line ->
            line.split(" ")
                .map { it.toInt() }
        }.count {
            val safe = isSafeTailed(it[0], it[1], it.drop(2), 0)
            if (!safe)
                failed.add(it)
            safe
        }
    }
    val secondPass = failed.count { row ->
        val tries = (0..row.lastIndex)
            .map { listWithElementAtRemoved(it, row) }
       // tries.map { println(it) }
        tries
            .any { isSafeTailed(it[0], it[1], it.drop(2), 0) }
    }
    println(firstPass + secondPass)
}
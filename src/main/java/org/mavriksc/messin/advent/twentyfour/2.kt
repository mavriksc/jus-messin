package org.mavriksc.messin.advent.twentyfour

import org.mavriksc.messin.toReader

fun main() {
    dayTwoPartOne()
}

fun dayTwoPartOne() {
    "advent/24/two/input".toReader().useLines { lines ->
        println(lines.filter { isValid(it) }.count())
    }
}
// for part 2
// if hit false need to check if either removing n-1  meaning (n-2 ->n) is valid as well as the rest of the list
// or remove n and then (n-1 -> n+1 ) is valid as well as the rest of the list


fun isValid(line: String): Boolean {
    val report = line.split(" ").map { it.toInt() }
    if (report[0] == report[1]) {
        return false
    }
    val isIncreasing = report[0] < report[1]
    report.drop(1).forEachIndexed { index, current ->
        val prior = report[index]
        println("Is increasing:$isIncreasing Current:$current Prior:$prior")
        if (isIncreasing) {
            if (current <= prior || current - prior > 3)
                return false
        } else if (current >= prior || prior - current > 3)
            return false
    }
    return true
}

package org.mavriksc.messin.advent.five

fun main() {
    val input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
            "824824821-824824827,2121212118-2121212124"
    val count =input.split(",").sumOf {
        val ids = it.split("-")
        countInvalidInRange(ids[0], ids[1])
    }
    println(count)
}

fun countInvalidInRange(start: String, end: String): Int {
    // if start is odd length fist is one longer "10..."
    // if end is odd length last is one shorter and "99.."
    val startOdd = start.length % 2 == 1
    val endOdd = end.length % 2 == 1
    val adjustedStart = if (startOdd) "1${"0".repeat(start.length )}" else start
    val adjustedEnd = if (endOdd) "9".repeat(end.length - 1) else end


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



    val a = adjustedStart.substring(0, partMinLen).toInt()
    val b = adjustedStart.substring(partMinLen, adjustedStart.length).toInt()
    val c = adjustedEnd.substring(0, partMaxLen).toInt()
    val d = adjustedEnd.substring(partMaxLen, adjustedEnd.length).toInt()
    val first = if (a >= b) a else a + 1
    val last = if (c <= d) c else c - 1

    return (first..last).sumOf { it.toString().repeat(2).toInt() }
}
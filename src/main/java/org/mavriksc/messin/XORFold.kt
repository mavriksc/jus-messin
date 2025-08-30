package org.mavriksc.messin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking


fun main() {

    val o = List(10_000_000) { it + 1 }
    val l = o.shuffled().dropLast(2)
    measureExecutionTime("findTwoMissing") { findTwoMissing(o, l) }
    measureExecutionTime("findTwoMissingFlows") { findTwoMissingFlows(o, l) }
    measureExecutionTime("findTwoMissingParallel") { findTwoMissingParallel(o, l) }
}

fun findOneMissing() {
    val o = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val l = o.shuffled().dropLast(1)
    println(l)
    println(o.reduce { a, b -> a xor b } xor l.reduce { a, b -> a xor b })
}

fun findTwoMissing(o: List<Int>, l: List<Int>) {
    fun Collection<Int>.xOrReduce() = this.reduce { a, b -> a xor b }
    val uXv = o.xOrReduce() xor l.xOrReduce()// the 2 values compressed as u xor v
    val lsb = uXv and -uXv // find a place where u and v are different lsb is fine
    //use 0 otherwise it can create an empty partition
    val po =
        o.filter { it and lsb == 0 }.xOrReduce()// get partition of the original list where lsb of 'it' is 0 and reduce
    val pl =
        l.filter { it and lsb == 0 }.xOrReduce()// get partition of the missing list where lsb of 'it' is 0 and reduce
    val v = po xor pl // it's now 1 value missing in 1 list
    val u = uXv xor v // get u now that we know v
    //println(l)
    println("$u, $v")

}


@OptIn(ExperimentalCoroutinesApi::class)
fun findTwoMissingFlows(o: List<Int>, l: List<Int>) {
    suspend fun Collection<Int>.xOrReduce() = this.asFlow().reduce { a, b -> a xor b }
    suspend fun Collection<Int>.filterLSBAndReduce(lsb: Int): Int =
        this.asFlow().filter { it and lsb == 0 }.reduce { a, b -> a xor b }

    val uxv: Int = runBlocking {
        run { o.xOrReduce() } xor run { l.xOrReduce() }
    }
    val lsb: Int = uxv and -uxv
    val v: Int = runBlocking {
        run { o.filterLSBAndReduce(lsb) } xor run { l.filterLSBAndReduce(lsb) }
    }
    val u = uxv xor v
    println("$u, $v")
}

fun findTwoMissingParallel(o: List<Int>, l: List<Int>) {
    fun Collection<Int>.reduceParallel(): Int = this.parallelStream().reduce { a, b -> a xor b }.get()
    fun Collection<Int>.filterReduceParallel(lsb: Int) =
        this.parallelStream().filter { it and lsb == 0 }.reduce { a, b -> a xor b }.get()

    val uxv = o.reduceParallel() xor l.reduceParallel()
    val lsb = uxv and -uxv
    val po = o.filterReduceParallel(lsb)
    val pl = l.filterReduceParallel(lsb)
    val v = po xor pl
    val u = uxv xor v
    println("$u, $v")
}

fun measureExecutionTime(functionName: String, block: () -> Unit) {
    val startTime = System.nanoTime()
    block()
    val endTime = System.nanoTime()
    println("$functionName took ${(endTime - startTime) / 1_000_000.0} ms")
}
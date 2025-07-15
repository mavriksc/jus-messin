package org.mavriksc.messin

fun main() {
 findTwoMissing()
}

fun findOneMissing(){
    val o = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val l = o.shuffled().dropLast(1)
    println(l)
    println(o.reduce { a, b -> a xor b } xor l.reduce { a, b -> a xor b })
}

fun findTwoMissing(){
    val o = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val l = o.shuffled().dropLast(2)
    val uXv = o.reduce { a, b -> a xor b } xor l.reduce { a, b -> a xor b } // the 2 values compressed as u xor v
    val lsb = uXv and -uXv // find a place where u and v are different lsb is fine
    val pot = o.filter { it and lsb == 1 }.reduce { a, b -> a xor b }// get partition of the original list where lsb is 1 and reduce
    val pom = l.filter { it and lsb == 1 }.reduce { a, b -> a xor b }// get partition of the missing list where lsb is 1 and reduce
    val v = pot xor pom // it's now 1 value missing in 1 list
    val u = uXv xor v // get u now that we know v
    println(l)
    println("$u, $v")

}